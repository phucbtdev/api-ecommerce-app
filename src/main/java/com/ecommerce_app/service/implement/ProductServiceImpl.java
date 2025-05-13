package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.ProductCreationRequest;
import com.ecommerce_app.dto.request.ProductUpdateRequest;
import com.ecommerce_app.dto.response.ProductBasicResponse;
import com.ecommerce_app.dto.response.ProductResponse;
import com.ecommerce_app.entity.*;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.ProductMapper;
import com.ecommerce_app.repository.*;
import com.ecommerce_app.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreationRequest request) {
        // Generate slug if not provided
        if (request.getSlug() == null || request.getSlug().isEmpty()) {
            request.setSlug(generateSlug(request.getName()));
        }

        // Verify slug uniqueness
        if (productRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Product with this slug already exists");
        }

        // Verify SKU uniqueness if provided
        if (request.getSku() != null && !request.getSku().isEmpty() &&
                productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("Product with this SKU already exists");
        }

        // Fetch categories
        Set<Category> categories = fetchCategories(request.getCategoryIds());

        // Fetch tags
        Set<Tag> tags = fetchTags(request.getTagIds());

        // Map DTO to entity
        Product product = productMapper.toEntity(request);
        product.setCategories(categories);
        product.setTags(tags);

        // Set default values if needed
        if (product.getActive() == null) {
            product.setActive(true);
        }

        // Save product
        product = productRepository.save(product);

        // Process variants and images (these would be handled by their respective services)
        // This would be implemented in a real application with proper services

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UUID id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Verify slug uniqueness if changing
        if (request.getSlug() != null && !request.getSlug().equals(product.getSlug()) &&
                productRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Product with this slug already exists");
        }

        // Verify SKU uniqueness if changing
        if (request.getSku() != null && !request.getSku().equals(product.getSku()) &&
                !request.getSku().isEmpty() && productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("Product with this SKU already exists");
        }

        // Update categories if provided
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<Category> categories = fetchCategories(request.getCategoryIds());
            product.setCategories(categories);
        }

        // Update tags if provided
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            Set<Tag> tags = fetchTags(request.getTagIds());
            product.setTags(tags);
        }

        // Update fields from DTO
        productMapper.updateEntityFromDto(request, product);
        product.setUpdatedAt(LocalDateTime.now());

        // Save updated product
        product = productRepository.save(product);

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductBasicResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toBasicDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductBasicResponse> getActiveProducts(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable)
                .map(productMapper::toBasicDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductBasicResponse> getProductsByCategory(UUID categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(productMapper::toBasicDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductBasicResponse> getProductsByTag(UUID tagId, Pageable pageable) {
        return productRepository.findByTagId(tagId, pageable)
                .map(productMapper::toBasicDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductBasicResponse> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchByKeyword(keyword, pageable)
                .map(productMapper::toBasicDto);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean toggleProductActive(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setActive(!product.getActive());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
        return product.getActive();
    }

    private String generateSlug(String name) {
        // Simple slug generation - in a real app this would be more sophisticated
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }

    private Set<Category> fetchCategories(Set<UUID> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }

        Set<Category> categories = categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toSet());

        if (categories.size() != categoryIds.size()) {
            throw new ResourceNotFoundException("One or more categories not found");
        }

        return categories;
    }

    private Set<Tag> fetchTags(Set<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new HashSet<>();
        }

        Set<Tag> tags = tagRepository.findAllById(tagIds)
                .stream()
                .collect(Collectors.toSet());

        if (tags.size() != tagIds.size()) {
            throw new ResourceNotFoundException("One or more tags not found");
        }

        return tags;
    }
}