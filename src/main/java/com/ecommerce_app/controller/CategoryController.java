package com.ecommerce_app.controller;


import com.ecommerce_app.dto.request.CategoryCreationRequest;
import com.ecommerce_app.dto.request.CategoryUpdateRequest;
import com.ecommerce_app.dto.response.ApiResponse;
import com.ecommerce_app.dto.response.CategoryResponse;
import com.ecommerce_app.dto.response.PagedResponse;
import com.ecommerce_app.service.interfaces.CategoryService;
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

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<PagedResponse<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        PagedResponse<CategoryResponse> response = categoryService.getAllCategories(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<CategoryResponse>> searchCategories(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PagedResponse<CategoryResponse> response = categoryService.searchCategories(name, isActive, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllActiveCategories() {
        List<CategoryResponse> categories = categoryService.getAllActiveCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable Long id
    ) {

        CategoryResponse categoryCreationRequest = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(categoryCreationRequest));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlug(
            @PathVariable String slug
    ) {

        CategoryResponse categoryCreationRequest = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(categoryCreationRequest));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryCreationRequest categoryCreationRequest
    ) {

        CategoryResponse createdCategory = categoryService.createCategory(categoryCreationRequest);
        return new ResponseEntity<>(ApiResponse.success("Category created successfully", createdCategory),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest categoryCreationRequest
    ) {

        CategoryResponse updatedCategory = categoryService.updateCategory(id, categoryCreationRequest);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long id
    ) {

        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<CategoryResponse>> toggleCategoryStatus(
            @PathVariable Long id
    ) {

        CategoryResponse categoryResponse = categoryService.toggleCategoryStatus(id);
        String message = categoryResponse.getIsActive()
                ? "Category activated successfully"
                : "Category deactivated successfully";
        return ResponseEntity.ok(ApiResponse.success(message, categoryResponse));
    }
}
