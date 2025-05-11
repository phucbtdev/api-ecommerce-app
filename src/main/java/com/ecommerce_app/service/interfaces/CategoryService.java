package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.CategoryCreationRequest;
import com.ecommerce_app.dto.request.CategoryUpdateRequest;
import com.ecommerce_app.dto.response.CategoryBasicResponse;
import com.ecommerce_app.dto.response.CategoryResponse;
import com.ecommerce_app.dto.response.CategoryTreeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse createCategory(CategoryCreationRequest request);

    CategoryResponse updateCategory(UUID id, CategoryUpdateRequest request);

    CategoryResponse getCategoryById(UUID id);

    CategoryResponse getCategoryBySlug(String slug);

    Page<CategoryResponse> getAllCategories(Pageable pageable);

    List<CategoryBasicResponse> getAllCategoriesBasic();

    List<CategoryTreeResponse> getCategoryTree();

    List<CategoryResponse> getSubcategories(UUID parentId);

    void deleteCategory(UUID id);

    CategoryResponse moveCategory(UUID categoryId, UUID newParentId);
}
