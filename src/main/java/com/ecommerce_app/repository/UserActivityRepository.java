package com.ecommerce_app.repository;

import com.ecommerce_app.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserActivityRepository extends JpaRepository<UserActivity, UUID> {
}
