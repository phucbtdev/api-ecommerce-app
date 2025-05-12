package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.ShippingCreationRequest;
import com.ecommerce_app.dto.request.ShippingUpdateRequest;
import com.ecommerce_app.dto.response.ShippingResponse;
import com.ecommerce_app.service.interfaces.ShippingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shippings")
@RequiredArgsConstructor
@Validated
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping
    public ResponseEntity<ShippingResponse> createShipping(@RequestBody @Valid ShippingCreationRequest shippingCreationRequest) {
        return new ResponseEntity<>(shippingService.createShipping(shippingCreationRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingResponse> updateShipping(@PathVariable Long id,
                                                           @RequestBody @Valid ShippingUpdateRequest shippingUpdateRequest) {
        return ResponseEntity.ok(shippingService.updateShipping(id, shippingUpdateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingResponse> getShippingById(@PathVariable Long id) {
        return ResponseEntity.ok(shippingService.getShippingById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShippingResponse> getShippingByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(shippingService.getShippingByOrderId(orderId));
    }

    @GetMapping
    public ResponseEntity<List<ShippingResponse>> getAllShippings() {
        return ResponseEntity.ok(shippingService.getAllShippings());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipping(@PathVariable Long id) {
        shippingService.deleteShipping(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShippingResponse> updateShippingStatus(
            @PathVariable Long id,
            @RequestParam @NotBlank(message = "Status cannot be blank") String status) {
        return ResponseEntity.ok(shippingService.updateShippingStatus(id, status));
    }

    @PatchMapping("/{id}/tracking")
    public ResponseEntity<ShippingResponse> updateTrackingInfo(
            @PathVariable Long id,
            @RequestParam @NotBlank(message = "Tracking number cannot be blank") String trackingNumber,
            @RequestParam @NotBlank(message = "Carrier cannot be blank") String carrier) {
        return ResponseEntity.ok(shippingService.updateTrackingInfo(id, trackingNumber, carrier));
    }

    @PatchMapping("/{id}/delivery-date")
    public ResponseEntity<ShippingResponse> updateDeliveryDate(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean isActual) {
        return ResponseEntity.ok(shippingService.updateDeliveryDate(id, isActual));
    }

    @GetMapping("/exists/order/{orderId}")
    public ResponseEntity<Boolean> existsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(shippingService.existsByOrderId(orderId));
    }
}
