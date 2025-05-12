package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.CouponCreationRequest;
import com.ecommerce_app.dto.request.CouponUpdateRequest;
import com.ecommerce_app.dto.response.CouponResponse;
import com.ecommerce_app.entity.Coupon;
import com.ecommerce_app.exception.DuplicateResourceException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.CouponMapper;
import com.ecommerce_app.repository.CouponRepository;
import com.ecommerce_app.service.interfaces.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public CouponResponse createCoupon(CouponCreationRequest request) {
        // Validate coupon code is unique
        if (couponRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Coupon with code " + request.getCode() + " already exists");
        }

        // Create new coupon
        Coupon coupon = couponMapper.toEntity(request);

        // Save and return
        Coupon savedCoupon = couponRepository.save(coupon);
        return couponMapper.toResponse(savedCoupon);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponResponse getCouponById(UUID id) {
        Coupon coupon = findCouponById(id);
        return couponMapper.toResponse(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponResponse getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon with code " + code + " not found"));
        return couponMapper.toResponse(coupon);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(UUID id, CouponUpdateRequest request) {
        Coupon existingCoupon = findCouponById(id);

        // Check if code is being updated and if it's unique
        if (request.getCode() != null && !request.getCode().equals(existingCoupon.getCode())
                && couponRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Coupon with code " + request.getCode() + " already exists");
        }

        // Update existing coupon
        couponMapper.updateEntityFromDto(request, existingCoupon);

        // Save and return
        Coupon updatedCoupon = couponRepository.save(existingCoupon);
        return couponMapper.toResponse(updatedCoupon);
    }

    @Override
    @Transactional
    public void deleteCoupon(UUID id) {
        if (!couponRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coupon with id " + id + " not found");
        }
        couponRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(couponMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponse> getActiveCoupons() {
        return couponRepository.findByActive(true).stream()
                .map(couponMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponse> getValidCoupons() {
        LocalDateTime now = LocalDateTime.now();
        return couponRepository.findByActiveAndValidFromBeforeAndValidUntilAfter(true, now, now).stream()
                .map(couponMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponse> getExpiredCoupons() {
        LocalDateTime now = LocalDateTime.now();
        return couponRepository.findByValidUntilBefore(now).stream()
                .map(couponMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponse> getAvailableCoupons() {
        LocalDateTime now = LocalDateTime.now();
        List<Coupon> validCoupons = couponRepository.findByActiveAndValidFromBeforeAndValidUntilAfter(true, now, now);

        return validCoupons.stream()
                .filter(coupon -> coupon.getUsageLimit() == null || coupon.getUsedCount() < coupon.getUsageLimit())
                .map(couponMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CouponResponse deactivateCoupon(UUID id) {
        Coupon coupon = findCouponById(id);
        coupon.setActive(false);
        return couponMapper.toResponse(couponRepository.save(coupon));
    }

    @Override
    @Transactional
    public CouponResponse activateCoupon(UUID id) {
        Coupon coupon = findCouponById(id);
        coupon.setActive(true);
        return couponMapper.toResponse(couponRepository.save(coupon));
    }

    @Override
    @Transactional
    public CouponResponse incrementUsedCount(UUID id) {
        Coupon coupon = findCouponById(id);
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        return couponMapper.toResponse(couponRepository.save(coupon));
    }

    // Helper methods
    private Coupon findCouponById(UUID id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon with id " + id + " not found"));
    }
}
