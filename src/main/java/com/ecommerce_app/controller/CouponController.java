package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.CouponCreationRequest;
import com.ecommerce_app.dto.request.CouponUpdateRequest;
import com.ecommerce_app.dto.response.CouponResponse;
import com.ecommerce_app.service.interfaces.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CouponCreationRequest request) {
        return new ResponseEntity<>(couponService.createCoupon(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> getCouponById(@PathVariable UUID id) {
        return ResponseEntity.ok(couponService.getCouponById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CouponResponse> getCouponByCode(@PathVariable String code) {
        return ResponseEntity.ok(couponService.getCouponByCode(code));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponse> updateCoupon(
            @PathVariable UUID id,
            @Valid @RequestBody CouponUpdateRequest request) {
        return ResponseEntity.ok(couponService.updateCoupon(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable UUID id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CouponResponse>> getActiveCoupons() {
        return ResponseEntity.ok(couponService.getActiveCoupons());
    }

    @GetMapping("/valid")
    public ResponseEntity<List<CouponResponse>> getValidCoupons() {
        return ResponseEntity.ok(couponService.getValidCoupons());
    }

    @GetMapping("/expired")
    public ResponseEntity<List<CouponResponse>> getExpiredCoupons() {
        return ResponseEntity.ok(couponService.getExpiredCoupons());
    }

    @GetMapping("/available")
    public ResponseEntity<List<CouponResponse>> getAvailableCoupons() {
        return ResponseEntity.ok(couponService.getAvailableCoupons());
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CouponResponse> deactivateCoupon(@PathVariable UUID id) {
        return ResponseEntity.ok(couponService.deactivateCoupon(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CouponResponse> activateCoupon(@PathVariable UUID id) {
        return ResponseEntity.ok(couponService.activateCoupon(id));
    }

    @PatchMapping("/{id}/increment-usage")
    public ResponseEntity<CouponResponse> incrementUsedCount(@PathVariable UUID id) {
        return ResponseEntity.ok(couponService.incrementUsedCount(id));
    }
}
