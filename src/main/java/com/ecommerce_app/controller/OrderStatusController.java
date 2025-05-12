package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.OrderStatusCreationRequest;
import com.ecommerce_app.dto.request.OrderStatusUpdateRequest;
import com.ecommerce_app.dto.response.OrderStatusResponse;
import com.ecommerce_app.service.interfaces.OrderStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-statuses")
@RequiredArgsConstructor
public class OrderStatusController {
    private final OrderStatusService service;

    @PostMapping
    public ResponseEntity<OrderStatusResponse> create(@Valid @RequestBody OrderStatusCreationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrderStatus(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getOrderStatus(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderStatusResponse>> getAll() {
        return ResponseEntity.ok(service.getAllOrderStatuses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderStatusResponse> update(@PathVariable UUID id,
                                                      @Valid @RequestBody OrderStatusUpdateRequest request) {
        return ResponseEntity.ok(service.updateOrderStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteOrderStatus(id);
        return ResponseEntity.noContent().build();
    }
}
