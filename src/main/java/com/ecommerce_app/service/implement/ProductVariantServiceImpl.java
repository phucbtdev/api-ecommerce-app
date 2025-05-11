package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.ProductVariantCreationRequest;
import com.ecommerce_app.dto.request.ProductVariantUpdateRequest;
import com.ecommerce_app.dto.response.ProductVariantResponse;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.entity.ProductVariant;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.ProductVariantMapper;
import com.ecommerce_app.repository.ProductRepository;
import com.ecommerce_app.repository.ProductVariantRepository;
import com.ecommerce_app.service.interfaces.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final ProductVariantMapper variantMapper;

    @Override
    @Transactional
    public ProductVariantResponse createProductVariant(UUID productId, ProductVariantCreationRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Verify SKU uniqueness if provided
        if (request.getSku() != null && !request.getSku().isEmpty() &&
                variantRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("Product variant with this SKU already exists");
        }

        // Map DTO to entity
        ProductVariant variant = variantMapper.toEntity(request);
        variant.setProduct(product);

        // Save variant
        variant = variantRepository.save(variant);

        return variantMapper.toDto(variant);
    }

    @Override
    @Transactional
    public ProductVariantResponse updateProductVariant(UUID id, ProductVariantUpdateRequest request) {
        ProductVariant variant = variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with id: " + id));

        // Verify SKU uniqueness if changing
        if (request.getSku() != null && !request.getSku().equals(variant.getSku()) &&
                !request.getSku().isEmpty() && variantRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("Product variant with this SKU already exists");
        }

        // Update fields from DTO
        variantMapper.updateEntityFromDto(request, variant);
        variant.setUpdatedAt(LocalDateTime.now());

        // Save updated variant
        variant = variantRepository.save(variant);

        return variantMapper.toDto(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantResponse getProductVariantById(UUID id) {
        ProductVariant variant = variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with id: " + id));
        return variantMapper.toDto(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getVariantsByProductId(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        return variantRepository.findByProductId(productId)
                .stream()
                .map(variantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProductVariant(UUID id) {
        if (!variantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product variant not found with id: " + id);
        }
        variantRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getVariantsByProductIdAndAttribute(
            UUID productId, String attributeName, String attributeValue) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        return variantRepository.findByProductIdAndAttribute(productId, attributeName, attributeValue)
                .stream()
                .map(variantMapper::toDto)
                .collect(Collectors.toList());
    }
}