package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.VariantAttributeCreationRequest;
import com.ecommerce_app.dto.request.VariantAttributeUpdateRequest;
import com.ecommerce_app.dto.response.VariantAttributeResponse;
import com.ecommerce_app.entity.ProductVariant;
import com.ecommerce_app.entity.VariantAttribute;
import com.ecommerce_app.mapper.VariantAttributeMapper;
import com.ecommerce_app.repository.ProductVariantRepository;
import com.ecommerce_app.repository.VariantAttributeRepository;
import com.ecommerce_app.service.interfaces.VariantAttributeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VariantAttributeServiceImpl implements VariantAttributeService {

    private final VariantAttributeRepository repository;
    private final ProductVariantRepository variantRepository;
    private final VariantAttributeMapper mapper;

    @Override
    public VariantAttributeResponse create(VariantAttributeCreationRequest request) {
        ProductVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found with id: " + request.getVariantId()));

        VariantAttribute entity = mapper.toEntity(request, variant);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public VariantAttributeResponse findById(UUID id) {
        VariantAttribute entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VariantAttribute not found with id: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public VariantAttributeResponse update(UUID id, VariantAttributeUpdateRequest request) {
        VariantAttribute entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VariantAttribute not found with id: " + id));

        mapper.updateEntityFromRequest(request, entity);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("VariantAttribute not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
