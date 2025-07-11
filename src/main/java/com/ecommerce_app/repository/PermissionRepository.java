package com.ecommerce_app.repository;

import com.ecommerce_app.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByName(String name);

    boolean existsByName(String name);

    Set<Permission> findByNameIn(Set<String> names);
}