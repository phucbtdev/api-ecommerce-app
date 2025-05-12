package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.OrderItemCreationRequest;
import com.ecommerce_app.dto.request.OrderItemUpdateRequest;
import com.ecommerce_app.dto.response.OrderItemResponse;
import com.ecommerce_app.service.interfaces.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
@Slf4j
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping
    public ResponseEntity<OrderItemResponse> createOrderItem(
            @PathVariable UUID orderId,
            @Valid @RequestBody OrderItemCreationRequest request) {
        log.info("Request to create order item for order ID: {}", orderId);
        OrderItemResponse response = orderItemService.createOrderItem(orderId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<OrderItemResponse>> createOrderItems(
            @PathVariable UUID orderId,
            @Valid @RequestBody List<OrderItemCreationRequest> requests) {
        log.info("Request to create multiple order items for order ID: {}", orderId);
        List<OrderItemResponse> responses = orderItemService.createOrderItems(orderId, requests);
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<OrderItemResponse> getOrderItem(
            @PathVariable UUID orderId,
            @PathVariable UUID itemId) {
        log.info("Request to get order item with ID: {} from order ID: {}", itemId, orderId);
        OrderItemResponse response = orderItemService.getOrderItemById(itemId);

        // Validate that item belongs to specified order
        if (!response.getOrderId().equals(orderId)) {
            log.warn("Order item with ID: {} does not belong to order ID: {}", itemId, orderId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> getOrderItems(
            @PathVariable UUID orderId) {
        log.info("Request to get all order items for order ID: {}", orderId);
        List<OrderItemResponse> responses = orderItemService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<OrderItemResponse> updateOrderItem(
            @PathVariable UUID orderId,
            @PathVariable UUID itemId,
            @Valid @RequestBody OrderItemUpdateRequest request) {
        log.info("Request to update order item with ID: {} for order ID: {}", itemId, orderId);

        // First get the item to verify it belongs to the order
        OrderItemResponse existingItem = orderItemService.getOrderItemById(itemId);
        if (!existingItem.getOrderId().equals(orderId)) {
            log.warn("Order item with ID: {} does not belong to order ID: {}", itemId, orderId);
            return ResponseEntity.notFound().build();
        }

        OrderItemResponse response = orderItemService.updateOrderItem(itemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable UUID orderId,
            @PathVariable UUID itemId) {
        log.info("Request to delete order item with ID: {} from order ID: {}", itemId, orderId);

        // First get the item to verify it belongs to the order
        OrderItemResponse existingItem = orderItemService.getOrderItemById(itemId);
        if (!existingItem.getOrderId().equals(orderId)) {
            log.warn("Order item with ID: {} does not belong to order ID: {}", itemId, orderId);
            return ResponseEntity.notFound().build();
        }

        orderItemService.deleteOrderItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllOrderItems(
            @PathVariable UUID orderId) {
        log.info("Request to delete all order items for order ID: {}", orderId);
        orderItemService.deleteOrderItemsByOrderId(orderId);
        return ResponseEntity.noContent().build();

    }
}