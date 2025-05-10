package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.ProductCreationRequest;
import com.ecommerce_app.dto.request.ProductUpdateRequest;
import com.ecommerce_app.dto.response.ApiResponse;
import com.ecommerce_app.dto.response.PagedResponse;
import com.ecommerce_app.dto.response.ProductResponse;
import com.ecommerce_app.service.interfaces.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PagedResponse<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        PagedResponse<ProductResponse> response = productService.getAllProducts(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<ProductResponse>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<ProductResponse> response = productService.searchProducts(
                name, categoryId, minPrice, maxPrice, isActive, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PagedResponse<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PagedResponse<ProductResponse> response = productService.getProductsByCategoryId(categoryId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(id)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success( productService.getProductBySlug(slug)));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySku(@PathVariable String sku) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductBySku(sku)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductCreationRequest productCreationRequest
    ) {
        return new ResponseEntity<>(ApiResponse.success("Product created successfully", productService.createProduct(productCreationRequest)),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest productCreationRequest
            ) {

        return ResponseEntity.ok(
                ApiResponse.success("Product updated successfully", productService.updateProduct(id, productCreationRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id
    ) {

        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<ProductResponse>> toggleProductStatus(@PathVariable Long id) {
        ProductResponse updatedProduct = productService.toggleProductStatus(id);
        String message = updatedProduct.getActive()
                ? "Product activated successfully"
                : "Product deactivated successfully";
        return ResponseEntity.ok(ApiResponse.success(message, updatedProduct));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        ProductResponse productResponse = productService.updateProductStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Product stock updated successfully", productResponse));
    }
}
