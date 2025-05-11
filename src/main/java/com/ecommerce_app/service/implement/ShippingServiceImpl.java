package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.ShippingCreationRequest;
import com.ecommerce_app.dto.request.ShippingUpdateRequest;
import com.ecommerce_app.dto.response.ShippingResponse;
import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.Shipping;
import com.ecommerce_app.exception.BusinessException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.ShippingMapper;
import com.ecommerce_app.repository.OrderRepository;
import com.ecommerce_app.repository.ShippingRepository;
import com.ecommerce_app.service.interfaces.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

    private final ShippingRepository shippingRepository;
    private final OrderRepository orderRepository;
    private final ShippingMapper shippingMapper;

    @Override
    @Transactional
    public ShippingResponse createShipping(ShippingCreationRequest shippingCreationRequest) {
        // Check if shipping already exists for this order
        if (shippingRepository.existsByOrderId(shippingCreationRequest.getOrderId())) {
            throw new BusinessException("Shipping already exists for order with ID: " + shippingCreationRequest.getOrderId());
        }

        Order order = orderRepository.findById(shippingCreationRequest.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + shippingCreationRequest.getOrderId()));

        Shipping shipping = shippingMapper.toEntity(shippingCreationRequest, order);
        Shipping savedShipping = shippingRepository.save(shipping);

        return shippingMapper.toDto(savedShipping);
    }

    @Override
    @Transactional
    public ShippingResponse updateShipping(Long id, ShippingUpdateRequest shippingUpdateRequest) {
        Shipping shipping = shippingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping not found with id: " + id));

        shippingMapper.updateShippingFromDto(shippingUpdateRequest, shipping);
        Shipping updatedShipping = shippingRepository.save(shipping);

        return shippingMapper.toDto(updatedShipping);
    }

    @Override
    @Transactional(readOnly = true)
    public ShippingResponse getShippingById(Long id) {
        Shipping shipping = shippingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping not found with id: " + id));

        return shippingMapper.toDto(shipping);
    }

    @Override
    @Transactional(readOnly = true)
    public ShippingResponse getShippingByOrderId(Long orderId) {
        Shipping shipping = shippingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping not found for order id: " + orderId));

        return shippingMapper.toDto(shipping);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShippingResponse> getAllShippings() {
        return shippingRepository.findAll().stream()
                .map(shippingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteShipping(Long id) {
        if (!shippingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Shipping not found with id: " + id);
        }

        shippingRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByOrderId(Long orderId) {
        return shippingRepository.existsByOrderId(orderId);
    }

    @Override
    @Transactional
    public ShippingResponse updateShippingStatus(Long id, String status) {
        Shipping shipping = shippingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping not found with id: " + id));

        shipping.setShippingStatus(status);
        Shipping updatedShipping = shippingRepository.save(shipping);

        return shippingMapper.toDto(updatedShipping);
    }

    @Override
    @Transactional
    public ShippingResponse updateTrackingInfo(Long id, String trackingNumber, String carrier) {
        Shipping shipping = shippingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping not found with id: " + id));

        shipping.setTrackingNumber(trackingNumber);
        shipping.setCarrier(carrier);
        Shipping updatedShipping = shippingRepository.save(shipping);

        return shippingMapper.toDto(updatedShipping);
    }

    @Override
    @Transactional
    public ShippingResponse updateDeliveryDate(Long id, boolean isActual) {
        Shipping shipping = shippingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping not found with id: " + id));

        LocalDateTime now = LocalDateTime.now();

        if (isActual) {
            shipping.setActualDeliveryDate(now);
            shipping.setShippingStatus("DELIVERED");
        } else {
            shipping.setEstimatedDeliveryDate(now);
        }

        Shipping updatedShipping = shippingRepository.save(shipping);

        return shippingMapper.toDto(updatedShipping);
    }
}
