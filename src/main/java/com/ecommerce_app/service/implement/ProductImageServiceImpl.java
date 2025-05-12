package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.ProductImageCreationRequest;
import com.ecommerce_app.dto.request.ProductImageUpdateRequest;
import com.ecommerce_app.dto.response.ProductImageResponse;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.entity.ProductImage;
import com.ecommerce_app.entity.ProductVariant;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.ProductImageMapper;
import com.ecommerce_app.repository.ProductImageRepository;
import com.ecommerce_app.repository.ProductRepository;
import com.ecommerce_app.repository.ProductVariantRepository;
import com.ecommerce_app.service.interfaces.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class  ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductImageMapper imageMapper;

    @Override
    @Transactional
    public ProductImageResponse createProductImage(UUID productId, ProductImageCreationRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Map DTO to entity
        ProductImage image = imageMapper.toEntity(request);
        image.setProduct(product);

        // Set variant if provided
        if (request.getVariantId() != null) {
            ProductVariant variant = variantRepository.findById(request.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with id: " + request.getVariantId()));

            if (!variant.getProduct().getId().equals(productId)) {
                throw new IllegalArgumentException("Variant does not belong to the specified product");
            }

            image.setVariant(variant);
        }

        // Set default values for sort order if needed
        if (image.getSortOrder() == null) {
            image.setSortOrder(getNextSortOrder(productId, request.getVariantId()));
        }

        // Handle main image flag
        if (Boolean.TRUE.equals(image.getIsMain())) {
            imageRepository.unsetMainImageExcept(productId, null);
        } else if (image.getIsMain() == null) {
            image.setIsMain(false);
        }

        // Save image
        image = imageRepository.save(image);

        return imageMapper.toDto(image);
    }

    @Override
    @Transactional
    public ProductImageResponse updateProductImage(UUID id, ProductImageUpdateRequest request) {
        ProductImage image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product image not found with id: " + id));

        // Update fields from DTO
        imageMapper.updateEntityFromDto(request, image);
        image.setUpdatedAt(LocalDateTime.now());

        // Handle main image flag if changed
        if (Boolean.TRUE.equals(request.getIsMain()) && !Boolean.TRUE.equals(image.getIsMain())) {
            imageRepository.unsetMainImageExcept(image.getProduct().getId(), image.getId());
        }

        // Save updated image
        image = imageRepository.save(image);

        return imageMapper.toDto(image);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductImageResponse getProductImageById(UUID id) {
        ProductImage image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product image not found with id: " + id));
        return imageMapper.toDto(image);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImageResponse> getImagesByProductId(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        return imageRepository.findByProductIdOrderBySortOrderAsc(productId)
                .stream()
                .map(imageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImageResponse> getImagesByVariantId(UUID variantId) {
        if (!variantRepository.existsById(variantId)) {
            throw new ResourceNotFoundException("Product variant not found with id: " + variantId);
        }

        return imageRepository.findByVariantIdOrderBySortOrderAsc(variantId)
                .stream()
                .map(imageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImageResponse> getProductLevelImages(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        return imageRepository.findProductLevelImages(productId)
                .stream()
                .map(imageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProductImage(UUID id) {
        if (!imageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product image not found with id: " + id);
        }
        imageRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductImageResponse setMainImage(UUID imageId) {
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Product image not found with id: " + imageId));

        // Unset main flag on all other images
        imageRepository.unsetMainImageExcept(image.getProduct().getId(), imageId);

        // Set this image as main
        image.setIsMain(true);
        image.setUpdatedAt(LocalDateTime.now());
        image = imageRepository.save(image);

        return imageMapper.toDto(image);
    }

    private Integer getNextSortOrder(UUID productId, UUID variantId) {
        List<ProductImage> existingImages;

        if (variantId != null) {
            existingImages = imageRepository.findByVariantId(variantId);
        } else {
            existingImages = imageRepository.findProductLevelImages(productId);
        }

        if (existingImages.isEmpty()) {
            return 0;
        }

        return existingImages.stream()
                .mapToInt(img -> img.getSortOrder() != null ? img.getSortOrder() : 0)
                .max()
                .orElse(0) + 1;
    }
}
