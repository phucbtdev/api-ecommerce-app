package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.ReviewCreationRequest;
import com.ecommerce_app.dto.request.ReviewUpdateRequest;
import com.ecommerce_app.dto.response.ReviewResponse;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.entity.Review;
import com.ecommerce_app.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    /**
     * Maps a ReviewCreationRequest DTO to a Review entity.
     *
     * @param request The ReviewCreationRequest DTO
     * @return The mapped Review entity
     */
    @Mapping(target = "product", source = "product")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "published", ignore = true)
    @Mapping(target = "verified", ignore = true)
    Review toEntity(ReviewCreationRequest request, Product product, User user);

    /**
     * Maps a Review entity to a ReviewResponse DTO.
     *
     * @param review The Review entity
     * @return The mapped ReviewResponse DTO
     */
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "user.id", target = "userId")
    ReviewResponse toResponse(Review review);

    /**
     * Updates a Review entity from a ReviewUpdateRequest DTO.
     *
     * @param request The ReviewUpdateRequest DTO
     * @param review  The Review entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ReviewUpdateRequest request, @MappingTarget Review review);
}