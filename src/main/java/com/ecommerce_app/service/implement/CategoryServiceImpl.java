package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.CategoryCreationRequest;
import com.ecommerce_app.dto.request.CategoryUpdateRequest;
import com.ecommerce_app.dto.response.CategoryBasicResponse;
import com.ecommerce_app.dto.response.CategoryResponse;
import com.ecommerce_app.dto.response.CategoryTreeResponse;
import com.ecommerce_app.entity.Category;
import com.ecommerce_app.exception.ResourceAlreadyExistsException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.CategoryMapper;
import com.ecommerce_app.repository.CategoryRepository;
import com.ecommerce_app.service.interfaces.CategoryService;
import com.ecommerce_app.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryCreationRequest request) {
        log.info("Creating new category with name: {}", request.getName());

        // Validate unique name
        if (categoryRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Category");
        }

        Category category = categoryMapper.toEntity(request);

        // Generate slug if not provided
        if (category.getSlug() == null || category.getSlug().isEmpty()) {
            category.setSlug(SlugUtil.generateSlug(category.getName()));
        }

        // Validate unique slug
        if (categoryRepository.existsBySlug(category.getSlug())) {
            category.setSlug(category.getSlug() + "-" + System.currentTimeMillis());
        }

        // Set parent category if provided
        if (request.getParentId() != null) {
            Category parentCategory = findCategoryById(request.getParentId());
            category.setParent(parentCategory);
        }

        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        category.setCreatedAt(now);
        category.setUpdatedAt(now);

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", savedCategory.getId());

        CategoryResponse response = categoryMapper.toResponse(savedCategory);
        response.setProductCount(0); // New category has no products

        return response;
    }

    @Override
    public CategoryResponse updateCategory(UUID id, CategoryUpdateRequest request) {
        log.info("Updating category with ID: {}", id);

        Category category = findCategoryById(id);

        // Check name uniqueness if changed
        if (request.getName() != null && !request.getName().equals(category.getName()) &&
                categoryRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Category");
        }

        // Update the slug if name is changed
        if (request.getName() != null && !request.getName().equals(category.getName()) &&
                (request.getSlug() == null || request.getSlug().isEmpty())) {
            request.setSlug(SlugUtil.generateSlug(request.getName()));
        }

        // Check slug uniqueness if changed
        if (request.getSlug() != null && !request.getSlug().equals(category.getSlug()) &&
                categoryRepository.existsBySlug(request.getSlug())) {
            request.setSlug(request.getSlug() + "-" + System.currentTimeMillis());
        }

        categoryMapper.updateEntityFromDto(request, category);

        // Update parent category if specified
        if (request.getParentId() != null) {
            // Cannot set self as parent
            if (request.getParentId().equals(id)) {
                throw new IllegalArgumentException("Category cannot be its own parent");
            }

            // Check for circular reference
            if (request.getParentId() != null && isCircularReference(id, request.getParentId())) {
                throw new IllegalArgumentException("Setting this parent would create a circular reference");
            }

            Category parentCategory = findCategoryById(request.getParentId());
            category.setParent(parentCategory);
        } else if (request.getParentId() == null && request.getParentId() != category.getParent().getId()) {
            // If parent ID is explicitly set to null, remove parent
            category.setParent(null);
        }

        // Update timestamp
        category.setUpdatedAt(LocalDateTime.now());

        Category updatedCategory = categoryRepository.save(category);
        log.info("Category updated successfully with ID: {}", updatedCategory.getId());

        CategoryResponse response = categoryMapper.toResponse(updatedCategory);
        response.setProductCount(categoryRepository.countProductsByCategoryId(id));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(UUID id) {
        log.info("Fetching category with ID: {}", id);
        Category category = findCategoryById(id);

        CategoryResponse response = categoryMapper.toResponse(category);
        response.setProductCount(categoryRepository.countProductsByCategoryId(id));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryBySlug(String slug) {
        log.info("Fetching category with slug: {}", slug);

        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category"));

        CategoryResponse response = categoryMapper.toResponse(category);
        response.setProductCount(categoryRepository.countProductsByCategoryId(category.getId()));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        log.info("Fetching all categories with pagination");

        return categoryRepository.findAll(pageable)
                .map(category -> {
                    CategoryResponse response = categoryMapper.toResponse(category);
                    response.setProductCount(categoryRepository.countProductsByCategoryId(category.getId()));
                    return response;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryBasicResponse> getAllCategoriesBasic() {
        log.info("Fetching all categories without pagination (basic info)");

        return categoryRepository.findAll().stream()
                .map(categoryMapper::toBasicResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTreeResponse> getCategoryTree() {
        log.info("Fetching category tree");

        // Get all root categories (categories without parent)
        List<Category> rootCategories = categoryRepository.findAllRootCategories();

        return rootCategories.stream()
                .map(categoryMapper::toTreeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getSubcategories(UUID parentId) {
        log.info("Fetching subcategories for parent ID: {}", parentId);

        // Verify parent exists
        findCategoryById(parentId);

        List<Category> subcategories = categoryRepository.findAllByParentId(parentId);

        return subcategories.stream()
                .map(category -> {
                    CategoryResponse response = categoryMapper.toResponse(category);
                    response.setProductCount(categoryRepository.countProductsByCategoryId(category.getId()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(UUID id) {
        log.info("Deleting category with ID: {}", id);

        Category category = findCategoryById(id);

        // Check if category has subcategories
        if (!category.getSubcategories().isEmpty()) {
            log.warn("Cannot delete category with ID: {} as it has subcategories", id);
            throw new IllegalStateException("Cannot delete category as it has subcategories");
        }

        // Check if category has products
        int productCount = categoryRepository.countProductsByCategoryId(id);
        if (productCount > 0) {
            log.warn("Cannot delete category with ID: {} as it has associated products", id);
            throw new IllegalStateException("Cannot delete category as it has associated products");
        }

        categoryRepository.delete(category);
        log.info("Category deleted successfully with ID: {}", id);
    }

    @Override
    public CategoryResponse moveCategory(UUID categoryId, UUID newParentId) {
        log.info("Moving category ID: {} to new parent ID: {}", categoryId, newParentId);

        Category category = findCategoryById(categoryId);

        // If newParentId is null, make it a root category
        if (newParentId == null) {
            category.setParent(null);
        } else {
            // Cannot set self as parent
            if (categoryId.equals(newParentId)) {
                throw new IllegalArgumentException("Category cannot be its own parent");
            }

            // Check for circular reference
            if (isCircularReference(categoryId, newParentId)) {
                throw new IllegalArgumentException("Moving to this parent would create a circular reference");
            }

            Category newParent = findCategoryById(newParentId);
            category.setParent(newParent);
        }

        category.setUpdatedAt(LocalDateTime.now());
        Category updatedCategory = categoryRepository.save(category);

        log.info("Category moved successfully with ID: {}", updatedCategory.getId());

        CategoryResponse response = categoryMapper.toResponse(updatedCategory);
        response.setProductCount(categoryRepository.countProductsByCategoryId(categoryId));

        return response;
    }

    // Helper methods

    private Category findCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException( "Category id"));
    }

    private boolean isCircularReference(UUID categoryId, UUID potentialParentId) {
        // If the potential parent is the category itself, it's circular
        if (categoryId.equals(potentialParentId)) {
            return true;
        }

        // Get the potential parent
        Category potentialParent = findCategoryById(potentialParentId);

        // Traverse up the hierarchy to check if we encounter the category
        Category current = potentialParent.getParent();
        while (current != null) {
            if (current.getId().equals(categoryId)) {
                return true;
            }
            current = current.getParent();
        }

        return false;
    }
}