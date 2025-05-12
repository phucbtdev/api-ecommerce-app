package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.OrderItemCreationRequest;
import com.ecommerce_app.dto.request.OrderItemUpdateRequest;
import com.ecommerce_app.dto.response.OrderItemResponse;
import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.OrderItem;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.entity.ProductVariant;
import com.ecommerce_app.exception.EntityNotFoundException;
import com.ecommerce_app.exception.InvalidOperationException;
import com.ecommerce_app.mapper.OrderItemMapper;
import com.ecommerce_app.repository.OrderItemRepository;
import com.ecommerce_app.repository.OrderRepository;
import com.ecommerce_app.repository.ProductRepository;
import com.ecommerce_app.repository.ProductVariantRepository;
import com.ecommerce_app.service.interfaces.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderItemResponse createOrderItem(UUID orderId, OrderItemCreationRequest request) {
        log.info("Creating order item for order ID: {} and product ID: {}", orderId, request.getProductId());

        // Validate order existence
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        // Check if order can be modified
        if (isOrderStatusFinal(order.getStatus().getName())) {
            throw new InvalidOperationException("Cannot add items to an order with status: " + order.getStatus().getName());
        }

        // Validate product existence
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + request.getProductId()));

        // Validate product variant if provided
        ProductVariant productVariant = null;
        if (request.getProductVariantId() != null) {
            productVariant = productVariantRepository.findById(request.getProductVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product variant not found with ID: " + request.getProductVariantId()));

            // Check if variant belongs to the product
            if (!productVariant.getProduct().getId().equals(product.getId())) {
                throw new InvalidOperationException("Product variant does not belong to the specified product");
            }
        }

        // Map request to entity
        OrderItem orderItem = orderItemMapper.toEntity(request);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setProductVariant(productVariant);

        // Set prices
        BigDecimal unitPrice = determineUnitPrice(product, productVariant);
        orderItem.setUnitPrice(unitPrice);
        orderItem.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(request.getQuantity())));

        // Save order item
        orderItem = orderItemRepository.save(orderItem);

        log.info("Order item created successfully for order: {}", order.getOrderNumber());
        return orderItemMapper.toResponse(orderItem);
    }

    @Override
    @Transactional
    public List<OrderItemResponse> createOrderItems(UUID orderId, List<OrderItemCreationRequest> requests) {
        log.info("Creating multiple order items for order ID: {}", orderId);

        List<OrderItemResponse> responses = new ArrayList<>();
        for (OrderItemCreationRequest request : requests) {
            responses.add(createOrderItem(orderId, request));
        }

        return responses;
    }

    @Override
    public OrderItemResponse getOrderItemById(UUID id) {
        log.info("Fetching order item with ID: {}", id);
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found with ID: " + id));

        return orderItemMapper.toResponse(orderItem);
    }

    @Override
    public List<OrderItemResponse> getOrderItemsByOrderId(UUID orderId) {
        log.info("Fetching order items for order ID: {}", orderId);

        // Validate order existence
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found with ID: " + orderId);
        }

        return orderItemRepository.findByOrderId(orderId).stream()
                .map(orderItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderItemResponse updateOrderItem(UUID id, OrderItemUpdateRequest request) {
        log.info("Updating order item with ID: {}", id);
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found with ID: " + id));

        Order order = orderItem.getOrder();

        // Check if order can be modified
        if (isOrderStatusFinal(order.getStatus().getName())) {
            throw new InvalidOperationException("Cannot update items for an order with status: " + order.getStatus().getName());
        }

        // Validate product variant if provided
        if (request.getProductVariantId() != null &&
                !request.getProductVariantId().equals(orderItem.getProductVariant() != null ? orderItem.getProductVariant().getId() : null)) {

            ProductVariant productVariant = productVariantRepository.findById(request.getProductVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product variant not found with ID: " + request.getProductVariantId()));

            // Check if variant belongs to the product
            if (!productVariant.getProduct().getId().equals(orderItem.getProduct().getId())) {
                throw new InvalidOperationException("Product variant does not belong to the product");
            }

            orderItem.setProductVariant(productVariant);
        }

        // Update quantity and variant info
        orderItem.setQuantity(request.getQuantity());
        if (request.getVariantInfo() != null) {
            orderItem.setVariantInfo(request.getVariantInfo());
        }

        // Recalculate prices
        BigDecimal unitPrice = determineUnitPrice(orderItem.getProduct(), orderItem.getProductVariant());
        orderItem.setUnitPrice(unitPrice);
        orderItem.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(request.getQuantity())));

        // Save updated order item
        orderItem = orderItemRepository.save(orderItem);

        log.info("Order item updated successfully for order: {}", order.getOrderNumber());
        return orderItemMapper.toResponse(orderItem);
    }

    @Override
    @Transactional
    public void deleteOrderItem(UUID id) {
        log.info("Deleting order item with ID: {}", id);
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found with ID: " + id));

        Order order = orderItem.getOrder();

        // Check if order can be modified
        if (isOrderStatusFinal(order.getStatus().getName())) {
            throw new InvalidOperationException("Cannot delete items from an order with status: " + order.getStatus().getName());
        }

        orderItemRepository.delete(orderItem);
        log.info("Order item deleted successfully");
    }

    @Override
    @Transactional
    public void deleteOrderItemsByOrderId(UUID orderId) {
        log.info("Deleting all order items for order ID: {}", orderId);

        // Validate order existence
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found with ID: " + orderId);
        }

        orderItemRepository.deleteByOrderId(orderId);
        log.info("All order items deleted for order ID: {}", orderId);
    }

    // Helper methods

    private BigDecimal determineUnitPrice(Product product, ProductVariant variant) {
        // If variant exists and has a price, use it
        if (variant != null && variant.getPrice() != null) {
            return variant.getPrice();
        }

        // Otherwise use product price
        return product.getPrice();
    }

    private boolean isOrderStatusFinal(String statusName) {
        return "Completed".equals(statusName) || "Cancelled".equals(statusName);
    }
}