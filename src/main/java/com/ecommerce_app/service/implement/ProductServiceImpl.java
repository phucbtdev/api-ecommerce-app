package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.ProductCreationRequest;
import com.ecommerce_app.dto.request.ProductUpdateRequest;
import com.ecommerce_app.dto.response.PagedResponse;
import com.ecommerce_app.dto.response.ProductResponse;
import com.ecommerce_app.entity.Category;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.exception.BadRequestException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.ProductMapper;
import com.ecommerce_app.repository.CategoryRepository;
import com.ecommerce_app.repository.ProductRepository;
import com.ecommerce_app.service.interfaces.ProductService;
import com.ecommerce_app.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ValidationService validationService;

    @Override
    public PagedResponse<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productsPage = productRepository.findAll(pageable);

        List<ProductResponse> content = productMapper.productsToProductResponses(productsPage.getContent());

        return PagedResponse.of(
                content,
                productsPage.getNumber(),
                productsPage.getSize(),
                productsPage.getTotalElements(),
                productsPage.getTotalPages(),
                productsPage.isLast()
        );
    }

    @Override
    public PagedResponse<ProductResponse> searchProducts(
            String name,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean isActive,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = productRepository.findByFilters(
                name, categoryId, minPrice, maxPrice, isActive, pageable);

        List<ProductResponse> content = productMapper.productsToProductResponses(productsPage.getContent());

        return PagedResponse.of(
                content,
                productsPage.getNumber(),
                productsPage.getSize(),
                productsPage.getTotalElements(),
                productsPage.getTotalPages(),
                productsPage.isLast()
        );
    }

    @Override
    public PagedResponse<ProductResponse> getProductsByCategoryId(Long categoryId, int page, int size) {
        // Check if category exists
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = productRepository.findByCategoryId(categoryId, pageable);

        List<ProductResponse> content = productMapper.productsToProductResponses(productsPage.getContent());

        return PagedResponse.of(
                content,
                productsPage.getNumber(),
                productsPage.getSize(),
                productsPage.getTotalElements(),
                productsPage.getTotalPages(),
                productsPage.isLast()
        );
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.productToProductResponse(product);
    }

    @Override
    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "slug", slug));
        return productMapper.productToProductResponse(product);
    }

    @Override
    public ProductResponse getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "sku", sku));
        return productMapper.productToProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreationRequest productCreationRequest) {
        // Validate category exists
        Category category = categoryRepository.findById(productCreationRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productCreationRequest.getCategoryId()));

        // Validate SKU uniqueness
        validationService.validateProductSkuUnique(productCreationRequest.getSku());

        // Generate slug if not provided
        if (productCreationRequest.getSlug() == null || productCreationRequest.getSlug().isEmpty()) {
            productCreationRequest.setSlug(generateSlug(productCreationRequest.getName()));
        } else {
            validationService.validateProductSlugUnique(productCreationRequest.getSlug());
        }

        Product product = productMapper.toEntity(productCreationRequest);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        // Check if category exists if it's being changed
        if (!product.getCategory().getId().equals(productUpdateRequest.getCategoryId())) {
            Category category = categoryRepository.findById(productUpdateRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productUpdateRequest.getCategoryId()));
            product.setCategory(category);
        }

        // Check if SKU is being changed and if it's unique
        if (!product.getSku().equals(productUpdateRequest.getSku())) {
            validationService.validateProductSkuUnique(productUpdateRequest.getSku());
        }

        // Generate slug if not provided or check if unique if changed
        if (productUpdateRequest.getSlug() == null || productUpdateRequest.getSlug().isEmpty()) {
            productUpdateRequest.setSlug(generateSlug(productUpdateRequest.getName()));
        } else if (!product.getSlug().equals(productUpdateRequest.getSlug())) {
            validationService.validateProductSlugUnique(productUpdateRequest.getSlug());
        }

        // Update fields
        product.setName(productUpdateRequest.getName());
        product.setDescription(productUpdateRequest.getDescription());
        product.setPrice(productUpdateRequest.getPrice());
        product.setQuantity(productUpdateRequest.getQuantity());
        product.setImageUrl(productUpdateRequest.getImageUrl());
        product.setSlug(productUpdateRequest.getSlug());
        product.setSku(productUpdateRequest.getSku());

        if (productUpdateRequest.getIsActive() != null) {
            product.setIsActive(productUpdateRequest.getIsActive());
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.productToProductResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productRepository.delete(product);
    }

    @Override
    @Transactional
    public ProductResponse toggleProductStatus(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setIsActive(!product.getIsActive());
        Product updatedProduct = productRepository.save(product);
        return productMapper.productToProductResponse(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProductStock(Long id, Integer quantity) {
        if (quantity < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setQuantity(quantity);
        Product updatedProduct = productRepository.save(product);
        return productMapper.productToProductResponse(updatedProduct);
    }

    private String generateSlug(String name) {
        String baseSlug = name.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "");

        String slug = baseSlug;
        int counter = 1;

        while (productRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }
}
