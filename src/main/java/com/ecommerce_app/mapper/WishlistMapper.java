package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.WishlistCreationRequest;
import com.ecommerce_app.dto.request.WishlistUpdateRequest;
import com.ecommerce_app.dto.response.WishlistResponse;
import com.ecommerce_app.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for Wishlist entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface WishlistMapper {

    /**
     * Converts WishlistCreationRequest to Wishlist entity.
     *
     * @param request the creation request
     * @return the Wishlist entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "products", ignore = true)
    Wishlist toEntity(WishlistCreationRequest request);

    /**
     * Converts Wishlist entity to WishlistResponse DTO.
     *
     * @param wishlist the Wishlist entity
     * @return the WishlistResponse DTO
     */
    @Mapping(source = "user.id", target = "userId")
    WishlistResponse toResponse(Wishlist wishlist);

    /**
     * Updates Wishlist entity from WishlistUpdateRequest.
     *
     * @param request the update request
     * @param wishlist the target Wishlist entity
     */
    void updateEntityFromRequest(WishlistUpdateRequest request, @MappingTarget Wishlist wishlist);
}
