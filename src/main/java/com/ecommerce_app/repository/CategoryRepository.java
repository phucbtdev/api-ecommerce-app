package com.ecommerce_app.repository;


import com.ecommerce_app.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    @Query("SELECT c FROM Category c WHERE c.isActive = true")
    List<Category> findAllActive();

    @Query("SELECT c FROM Category c WHERE c.isActive = true")
    Page<Category> findAllActive(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:isActive IS NULL OR c.isActive = :isActive)")
    Page<Category> findByFilters(@Param("name") String name, @Param("isActive") Boolean isActive, Pageable pageable);
}
