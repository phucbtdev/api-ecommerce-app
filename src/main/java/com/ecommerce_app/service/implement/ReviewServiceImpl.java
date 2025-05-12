package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.ReviewCreationRequest;
import com.ecommerce_app.dto.request.ReviewUpdateRequest;
import com.ecommerce_app.dto.response.ReviewResponse;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.entity.Review;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.mapper.ReviewMapper;
import com.ecommerce_app.repository.ProductRepository;
import com.ecommerce_app.repository.ReviewRepository;
import com.ecommerce_app.repository.UserRepository;
import com.ecommerce_app.service.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponse createReview(ReviewCreationRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Review review = reviewMapper.toEntity(request, product, user);
        review = reviewRepository.save(review);
        return reviewMapper.toResponse(review);
    }

    @Override
    public ReviewResponse getReview(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        return reviewMapper.toResponse(review);
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(UUID id, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        reviewMapper.updateEntityFromRequest(request, review);
        review = reviewRepository.save(review);
        return reviewMapper.toResponse(review);
    }

    @Override
    @Transactional
    public void deleteReview(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        reviewRepository.delete(review);
    }
}