package com.ecommerce_app.service.implement;


import com.ecommerce_app.dto.request.CreateOrderItemRequest;
import com.ecommerce_app.dto.request.CreateOrderRequest;
import com.ecommerce_app.dto.request.UpdateOrderStatusRequest;
import com.ecommerce_app.dto.response.OrderResponse;
import com.ecommerce_app.dto.response.PagedResponse;
import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.OrderItem;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.exception.BadRequestException;
import com.ecommerce_app.exception.InsufficientStockException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.OrderMapper;
import com.ecommerce_app.repository.OrderRepository;
import com.ecommerce_app.repository.ProductRepository;
import com.ecommerce_app.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        log.info("Creating new order for customer: {}", createOrderRequest.getCustomerEmail());

        // Convert request to entity
        Order order = orderMapper.createOrderRequestToOrder(createOrderRequest);

        // Generate unique order number
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        order.setOrderNumber("ORD-" + timestamp + "-" + uuid);

        // Process each order item
        List<OrderItem> orderItems = new ArrayList<>();
        for (CreateOrderItemRequest itemRequest : createOrderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemRequest.getProductId()));

            // Check if there's enough stock
            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new InsufficientStockException(
                        String.format("Not enough stock for product: %s. Available: %d, Requested: %d",
                                product.getName(), product.getQuantity(), itemRequest.getQuantity()));
            }

            // Create order item
            OrderItem orderItem = OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .unitPrice(product.getPrice())
                    .quantity(itemRequest.getQuantity())
                    .build();

            // Calculate subtotal
            orderItem.calculateSubtotal();
            orderItems.add(orderItem);

            // Update product stock (would be better with a dedicated inventory service)
            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());
            productRepository.save(product);
        }

        // Add items to order
        orderItems.forEach(order::addOrderItem);

        // Calculate total amount
        order.calculateTotalAmount();

        // Save order
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with order number: {}", savedOrder.getOrderNumber());

        // Return response
        return orderMapper.orderToOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        log.info("Fetching order with id: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return orderMapper.orderToOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        log.info("Fetching order with order number: {}", orderNumber);
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));
        return orderMapper.orderToOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<OrderResponse> getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Fetching all orders with pagination - page: {}, size: {}", pageNo, pageSize);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Order> orders = orderRepository.findAll(pageable);

        return createPageResponse(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<OrderResponse> getOrdersByStatus(Order.OrderStatus status, int pageNo, int pageSize) {
        log.info("Fetching orders with status: {}", status);

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Order> orders = orderRepository.findByStatus(status, pageable);

        return createPageResponse(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<OrderResponse> getOrdersByCustomerEmail(String customerEmail, int pageNo, int pageSize) {
        log.info("Fetching orders for customer email: {}", customerEmail);

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Order> orders = orderRepository.findByCustomerEmail(customerEmail, pageable);

        return createPageResponse(orders);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest updateOrderStatusRequest) {
        log.info("Updating order status for order id: {} to {}", id, updateOrderStatusRequest.getStatus());

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        order.setStatus(updateOrderStatusRequest.getStatus());
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.orderToOrderResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        log.info("Deleting order with id: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        // Check if order can be deleted (e.g., only PENDING orders)
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new BadRequestException("Cannot delete order that is not in PENDING status");
        }

        // Return items to inventory (would be better with a dedicated inventory service)
        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", item.getProductId()));

            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        orderRepository.delete(order);
        log.info("Order deleted successfully");
    }

    private PagedResponse<OrderResponse> createPageResponse(Page<Order> ordersPage) {
        List<Order> orders = ordersPage.getContent();
        List<OrderResponse> content = orderMapper.ordersToOrderResponses(orders);

        return PagedResponse.<OrderResponse>builder()
                .content(content)
                .page(ordersPage.getNumber())
                .size(ordersPage.getSize())
                .totalElements(ordersPage.getTotalElements())
                .totalPages(ordersPage.getTotalPages())
                .last(ordersPage.isLast())
                .build();
    }
}
