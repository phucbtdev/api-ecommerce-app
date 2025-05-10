package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.CreateOrderRequest;
import com.ecommerce_app.dto.request.UpdateOrderStatusRequest;
import com.ecommerce_app.dto.response.OrderResponse;
import com.ecommerce_app.dto.response.PagedResponse;
import com.ecommerce_app.entity.Order;
import com.ecommerce_app.service.interfaces.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest createOrderRequest
    ) {

        return new ResponseEntity<>(orderService.createOrder(createOrderRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByOrderNumber(
            @PathVariable String orderNumber
    ) {

        return ResponseEntity.ok(orderService.getOrderByOrderNumber(orderNumber));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        return ResponseEntity.ok(orderService.getAllOrders(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<PagedResponse<OrderResponse>> getOrdersByStatus(
            @PathVariable Order.OrderStatus status,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {

        return ResponseEntity.ok(orderService.getOrdersByStatus(status, pageNo, pageSize));
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<PagedResponse<OrderResponse>> getOrdersByCustomerEmail(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {

        return ResponseEntity.ok(orderService.getOrdersByCustomerEmail(email, pageNo, pageSize));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest updateOrderStatusRequest
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, updateOrderStatusRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long id
    ) {

        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
