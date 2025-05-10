package com.ecommerce_app.service.interfaces;


import com.ecommerce_app.dto.request.CategoryCreationRequest;
import com.ecommerce_app.dto.request.CategoryUpdateRequest;
import com.ecommerce_app.dto.response.CategoryResponse;
import com.ecommerce_app.dto.response.PagedResponse;
import java.util.List;

public interface CategoryService {

    PagedResponse<CategoryResponse> getAllCategories(int page, int size, String sortBy, String sortDir);

    PagedResponse<CategoryResponse> searchCategories(String name, Boolean isActive, int page, int size);

    List<CategoryResponse> getAllActiveCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse getCategoryBySlug(String slug);

    CategoryResponse createCategory(CategoryCreationRequest categoryCreationRequest);

    CategoryResponse updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest);

    void deleteCategory(Long id);

    CategoryResponse toggleCategoryStatus(Long id);
}
