package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.OrderStatusCreationRequest;
import com.ecommerce_app.dto.request.OrderStatusUpdateRequest;
import com.ecommerce_app.dto.response.OrderStatusResponse;
import com.ecommerce_app.entity.OrderStatus;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.OrderStatusMapper;
import com.ecommerce_app.repository.OrderStatusRepository;
import com.ecommerce_app.service.interfaces.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {
    private final OrderStatusRepository repository;
    private final OrderStatusMapper mapper;

    @Override
    @Transactional
    public OrderStatusResponse createOrderStatus(OrderStatusCreationRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Order status with name " + request.getName() + " already exists");
        }
        OrderStatus entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public OrderStatusResponse getOrderStatus(UUID id) {
        OrderStatus entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderStatus not found with id: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<OrderStatusResponse> getAllOrderStatuses() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderStatusResponse updateOrderStatus(UUID id, OrderStatusUpdateRequest request) {
        OrderStatus entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderStatus not found with id: " + id));

        if (request.getName() != null && !request.getName().equals(entity.getName())
                && repository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Order status with name " + request.getName() + " already exists");
        }

        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void deleteOrderStatus(UUID id) {
        OrderStatus entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderStatus not found with id: " + id));
        if (!entity.getOrders().isEmpty()) {
            throw new IllegalStateException("Cannot delete order status with associated orders");
        }
        repository.delete(entity);
    }
}
