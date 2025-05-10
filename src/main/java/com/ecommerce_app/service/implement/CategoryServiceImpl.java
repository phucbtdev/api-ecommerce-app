package com.ecommerce_app.service.implement;


import com.ecommerce_app.dto.request.CategoryCreationRequest;
import com.ecommerce_app.dto.request.CategoryUpdateRequest;
import com.ecommerce_app.dto.response.CategoryResponse;
import com.ecommerce_app.dto.response.PagedResponse;
import com.ecommerce_app.entity.Category;
import com.ecommerce_app.exception.BadRequestException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.CategoryMapper;
import com.ecommerce_app.repository.CategoryRepository;
import com.ecommerce_app.service.interfaces.CategoryService;
import com.ecommerce_app.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ValidationService validationService;

    @Override
    public PagedResponse<CategoryResponse> getAllCategories(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categoriesPage = categoryRepository.findAll(pageable);

        List<CategoryResponse> content = categoryMapper.toResponseList(categoriesPage.getContent());

        return PagedResponse.of(
                content,
                categoriesPage.getNumber(),
                categoriesPage.getSize(),
                categoriesPage.getTotalElements(),
                categoriesPage.getTotalPages(),
                categoriesPage.isLast()
        );
    }

    @Override
    public PagedResponse<CategoryResponse> searchCategories(String name, Boolean isActive, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoriesPage = categoryRepository.findByFilters(name, isActive, pageable);

        List<CategoryResponse> content = categoryMapper.toResponseList(categoriesPage.getContent());

        return PagedResponse.of(
                content,
                categoriesPage.getNumber(),
                categoriesPage.getSize(),
                categoriesPage.getTotalElements(),
                categoriesPage.getTotalPages(),
                categoriesPage.isLast()
        );
    }

    @Override
    public List<CategoryResponse> getAllActiveCategories() {
        List<Category> categories = categoryRepository.findAllActive();
        return categoryMapper.toResponseList(categories);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return categoryMapper.toEntity(category);
    }

    @Override
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "slug", slug));
        return categoryMapper.toEntity(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryCreationRequest categoryCreationRequest) {
        validationService.validateCategoryNameUnique(categoryCreationRequest.getName());

        if (categoryCreationRequest.getSlug() == null || categoryCreationRequest.getSlug().isEmpty()) {
            categoryCreationRequest.setSlug(generateSlug(categoryCreationRequest.getName()));
        } else {
            validationService.validateCategorySlugUnique(categoryCreationRequest.getSlug());
        }

        Category category = categoryMapper.toEntity(categoryCreationRequest);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toEntity(savedCategory);
    }

    @Transactional
    @Override
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        // Check if name is being changed and if it's unique
        if (!category.getName().equals(categoryUpdateRequest.getName())) {
            validationService.validateCategoryNameUnique(categoryUpdateRequest.getName());
        }

        // Generate slug if not provided or check if unique if changed
        if (categoryUpdateRequest.getSlug() == null || categoryUpdateRequest.getSlug().isEmpty()) {
            categoryUpdateRequest.setSlug(generateSlug(categoryUpdateRequest.getName()));
        } else if (!category.getSlug().equals(categoryUpdateRequest.getSlug())) {
            validationService.validateCategorySlugUnique(categoryUpdateRequest.getSlug());
        }

        // Update fields
        category.setName(categoryUpdateRequest.getName());
        category.setDescription(categoryUpdateRequest.getDescription());
        category.setSlug(categoryUpdateRequest.getSlug());

        if (categoryUpdateRequest.getIsActive() != null) {
            category.setIsActive(categoryUpdateRequest.getIsActive());
        }

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toEntity(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        // Check if category has products
        if (!category.getProducts().isEmpty()) {
            throw new BadRequestException("Cannot delete category with associated products");
        }

        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public CategoryResponse toggleCategoryStatus(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setIsActive(!category.getIsActive());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toEntity(updatedCategory);
    }

    private String generateSlug(String name) {
        String baseSlug = name.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "");

        String slug = baseSlug;
        int counter = 1;

        while (categoryRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }
}