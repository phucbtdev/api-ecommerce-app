package com.ecommerce_app.repository;

import com.ecommerce_app.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
