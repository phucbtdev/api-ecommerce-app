package com.ecommerce_app.service.implement.;

import com.ecommerce_app.dto.request.OrderCreationRequest;
import com.ecommerce_app.dto.request.OrderItemCreationRequest;
import com.ecommerce_app.dto.request.OrderUpdateRequest;
import com.ecommerce_app.dto.request.PaymentCreationRequest;
import com.ecommerce_app.dto.request.ShippingCreationRequest;
import com.ecommerce_app.dto.response.OrderResponse;
import com.ecommerce_app.entity.*;
import com.ecommerce_app.exception.EntityNotFoundException;
import com.ecommerce_app.exception.InvalidOperationException;
import com.ecommerce_app.mapper.OrderMapper;
import com.ecommerce_app.repository.*;
import com.ecommerce_app.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final CouponRepository couponRepository;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreationRequest request) {
        log.info("Creating new order for user with ID: {}", request.getUserId());

        // Validate user existence
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getUserId()));

        // Validate addresses
        Address billingAddress = addressRepository.findById(request.getBillingAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Billing address not found with ID: " + request.getBillingAddressId()));
        Address shippingAddress = addressRepository.findById(request.getShippingAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Shipping address not found with ID: " + request.getShippingAddressId()));

        // Check if addresses belong to the user
        if (!billingAddress.getUser().getId().equals(user.getId())) {
            throw new InvalidOperationException("Billing address does not belong to the user");
        }
        if (!shippingAddress.getUser().getId().equals(user.getId())) {
            throw new InvalidOperationException("Shipping address does not belong to the user");
        }

        // Get default order status (e.g., "Pending")
        OrderStatus defaultStatus = orderStatusRepository.findByName("Pending")
                .orElseThrow(() -> new EntityNotFoundException("Default order status 'Pending' not found"));

        // Map request to entity
        Order order = orderMapper.toEntity(request);
        order.setUser(user);
        order.setBillingAddress(billingAddress);
        order.setShippingAddress(shippingAddress);
        order.setStatus(defaultStatus);

        // Handle coupon if provided
        if (request.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(request.getCouponId())
                    .orElseThrow(() -> new EntityNotFoundException("Coupon not found with ID: " + request.getCouponId()));

            // Validate coupon (active, not expired, usage limit not reached)
            validateCoupon(coupon);
            order.setCoupon(coupon);

            // Increment coupon usage
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            couponRepository.save(coupon);
        }

        // Save the order to get the ID
        order = orderRepository.save(order);

        // Initialize amounts
        order.setTotalAmount(BigDecimal.ZERO);
        order.setShippingAmount(BigDecimal.ZERO);
        order.setTaxAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);

        // Create order items
        for (OrderItemCreationRequest itemRequest : request.getOrderItems()) {
            orderItemService.createOrderItem(order.getId(), itemRequest);
        }

        // Reload the order with items to calculate totals
        order = orderRepository.findById(order.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found after creation"));

        // Calculate order totals
        calculateOrderTotals(order);

        // Create payment
        createPaymentForOrder(order, request.getPaymentMethod(), request.getPaymentDetails());

        // Create shipping
        createShippingForOrder(order, request.getShippingMethod());

        // Save updated order
        order = orderRepository.save(order);

        log.info("Order created successfully with order number: {}", order.getOrderNumber());
        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse getOrderById(UUID id) {
        log.info("Fetching order by ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        log.info("Fetching order by order number: {}", orderNumber);
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with order number: " + orderNumber));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(UUID id, OrderUpdateRequest request) {
        log.info("Updating order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        // Check if order can be updated (not completed or cancelled)
        if (isOrderStatusFinal(order.getStatus().getName())) {
            throw new InvalidOperationException("Cannot update order with status: " + order.getStatus().getName());
        }

        // Validate status if provided
        if (request.getStatusId() != null) {
            OrderStatus status = orderStatusRepository.findById(request.getStatusId())
                    .orElseThrow(() -> new EntityNotFoundException("Order status not found with ID: " + request.getStatusId()));
            order.setStatus(status);
        }

        // Update billing address if provided
        if (request.getBillingAddressId() != null) {
            Address billingAddress = addressRepository.findById(request.getBillingAddressId())
                    .orElseThrow(() -> new EntityNotFoundException("Billing address not found with ID: " + request.getBillingAddressId()));

            if (!billingAddress.getUser().getId().equals(order.getUser().getId())) {
                throw new InvalidOperationException("Billing address does not belong to the order's user");
            }
            order.setBillingAddress(billingAddress);
        }

        // Update shipping address if provided
        if (request.getShippingAddressId() != null) {
            Address shippingAddress = addressRepository.findById(request.getShippingAddressId())
                    .orElseThrow(() -> new EntityNotFoundException("Shipping address not found with ID: " + request.getShippingAddressId()));

            if (!shippingAddress.getUser().getId().equals(order.getUser().getId())) {
                throw new InvalidOperationException("Shipping address does not belong to the order's user");
            }
            order.setShippingAddress(shippingAddress);
        }

        // Update customer notes if provided
        if (request.getCustomerNotes() != null) {
            order.setCustomerNotes(request.getCustomerNotes());
        }

        // Save updated order
        order = orderRepository.save(order);

        log.info("Order updated successfully: {}", order.getOrderNumber());
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public void deleteOrder(UUID id) {
        log.info("Deleting order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        // Check if order can be deleted (e.g., only if it has a specific status like "Pending")
        if (!order.getStatus().getName().equals("Pending")) {
            throw new InvalidOperationException("Cannot delete order with status: " + order.getStatus().getName());
        }

        // Delete related entities
        orderItemService.deleteOrderItemsByOrderId(id);
        orderRepository.delete(order);

        log.info("Order deleted successfully: {}", order.getOrderNumber());
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        log.info("Fetching all orders with pagination");
        return orderRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    public Page<OrderResponse> getOrdersByUser(UUID userId, Pageable pageable) {
        log.info("Fetching orders for user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        return orderRepository.findByUserOrderByCreatedAtDesc(user, pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    public Page<OrderResponse> getOrdersByStatus(UUID statusId, Pageable pageable) {
        log.info("Fetching orders with status ID: {}", statusId);
        OrderStatus status = orderStatusRepository.findById(statusId)
                .orElseThrow(() -> new EntityNotFoundException("Order status not found with ID: " + statusId));

        return orderRepository.findByStatusOrderByCreatedAtDesc(status, pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    public Page<OrderResponse> getOrdersByUserAndStatus(UUID userId, UUID statusId, Pageable pageable) {
        log.info("Fetching orders for user ID: {} with status ID: {}", userId, statusId);

        // Validate user and status existence
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
        if (!orderStatusRepository.existsById(statusId)) {
            throw new EntityNotFoundException("Order status not found with ID: " + statusId);
        }

        return orderRepository.findByUserIdAndStatusId(userId, statusId, pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    public List<OrderResponse> getOrdersInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching orders in date range from {} to {}", startDate, endDate);
        return orderRepository.findOrdersInDateRange(startDate, endDate).stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(UUID id, UUID statusId) {
        log.info("Updating status for order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        OrderStatus status = orderStatusRepository.findById(statusId)
                .orElseThrow(() -> new EntityNotFoundException("Order status not found with ID: " + statusId));

        // Check if current status is final
        if (isOrderStatusFinal(order.getStatus().getName())) {
            throw new InvalidOperationException("Cannot update order with final status: " + order.getStatus().getName());
        }

        order.setStatus(status);
        order = orderRepository.save(order);

        log.info("Order status updated successfully for order: {}", order.getOrderNumber());
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID id) {
        log.info("Cancelling order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        // Can only cancel if not already completed or cancelled
        if (isOrderStatusFinal(order.getStatus().getName())) {
            throw new InvalidOperationException("Cannot cancel order with status: " + order.getStatus().getName());
        }

        OrderStatus cancelledStatus = orderStatusRepository.findByName("Cancelled")
                .orElseThrow(() -> new EntityNotFoundException("Order status 'Cancelled' not found"));

        order.setStatus(cancelledStatus);
        order = orderRepository.save(order);

        log.info("Order cancelled successfully: {}", order.getOrderNumber());
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse completeOrder(UUID id) {
        log.info("Completing order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        // Can only complete if not already completed or cancelled
        if (isOrderStatusFinal(order.getStatus().getName())) {
            throw new InvalidOperationException("Cannot complete order with status: " + order.getStatus().getName());
        }

        OrderStatus completedStatus = orderStatusRepository.findByName("Completed")
                .orElseThrow(() -> new EntityNotFoundException("Order status 'Completed' not found"));

        order.setStatus(completedStatus);
        order = orderRepository.save(order);

        log.info("Order completed successfully: {}", order.getOrderNumber());
        return orderMapper.toResponse(order);
    }

    // Helper methods

    private void validateCoupon(Coupon coupon) {
        if (!coupon.getActive()) {
            throw new InvalidOperationException("Coupon is not active");
        }

        LocalDateTime now = LocalDateTime.now();
        if (coupon.getValidFrom() != null && now.isBefore(coupon.getValidFrom())) {
            throw new InvalidOperationException("Coupon is not yet valid");
        }

        if (coupon.getValidUntil() != null && now.isAfter(coupon.getValidUntil())) {
            throw new InvalidOperationException("Coupon has expired");
        }

        if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new InvalidOperationException("Coupon usage limit reached");
        }
    }

    private void calculateOrderTotals(Order order) {
        // Calculate items total
        BigDecimal itemsTotal = BigDecimal.ZERO;
        for (OrderItem item : order.getOrderItems()) {
            itemsTotal = itemsTotal.add(item.getTotalPrice());
        }

        // Set shipping amount (could be dynamic based on items, weight, etc.)
        BigDecimal shippingAmount = BigDecimal.valueOf(5.99); // Example fixed shipping cost
        order.setShippingAmount(shippingAmount);

        // Calculate tax (e.g., 8%)
        BigDecimal taxRate = BigDecimal.valueOf(0.08);
        BigDecimal taxAmount = itemsTotal.multiply(taxRate).setScale(2, java.math.RoundingMode.HALF_UP);
        order.setTaxAmount(taxAmount);

        // Calculate discount if coupon present
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (order.getCoupon() != null) {
            Coupon coupon = order.getCoupon();

            if ("PERCENTAGE".equals(coupon.getDiscountType())) {
                discountAmount = itemsTotal.multiply(
                        coupon.getDiscountValue().divide(BigDecimal.valueOf(100))
                ).setScale(2, java.math.RoundingMode.HALF_UP);
            } else if ("FIXED_AMOUNT".equals(coupon.getDiscountType())) {
                discountAmount = coupon.getDiscountValue();
            }

            // Apply minimum purchase check
            if (coupon.getMinimumPurchaseAmount() != null &&
                    itemsTotal.compareTo(coupon.getMinimumPurchaseAmount()) < 0) {
                discountAmount = BigDecimal.ZERO;
            }

            // Apply maximum discount check
            if (coupon.getMaximumDiscountAmount() != null &&
                    discountAmount.compareTo(coupon.getMaximumDiscountAmount()) > 0) {
                discountAmount = coupon.getMaximumDiscountAmount();
            }
        }
        order.setDiscountAmount(discountAmount);

        // Calculate total (items + shipping + tax - discount)
        BigDecimal totalAmount = itemsTotal.add(shippingAmount).add(taxAmount).subtract(discountAmount);
        order.setTotalAmount(totalAmount);
    }

    private void createPaymentForOrder(Order order, String paymentMethod, String paymentDetails) {
        PaymentCreationRequest paymentRequest = PaymentCreationRequest.builder()
                .orderId(order.getId())
                .paymentMethod(paymentMethod)
                .paymentStatus("Pending")
                .amount(order.getTotalAmount())
                .currency("USD") // Default currency
                .paymentDetails(paymentDetails)
                .build();

        paymentService.createPayment(paymentRequest);
    }

    private void createShippingForOrder(Order order, String shippingMethod) {
        ShippingCreationRequest shippingRequest = ShippingCreationRequest.builder()
                .orderId(order.getId())
                .shippingMethod(shippingMethod)
                .shippingCost(order.getShippingAmount())
                .shippingStatus("Preparing")
                .build();

        shippingService.createShipping(shippingRequest);
    }

    private boolean isOrderStatusFinal(String statusName) {
        return "Completed".equals(statusName) || "Cancelled".equals(statusName);
    }

}