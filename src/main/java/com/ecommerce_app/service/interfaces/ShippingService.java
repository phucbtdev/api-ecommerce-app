/**
 * Service interface that manages shipping operations in the e-commerce application.
 * <p>
 * This interface provides methods for creating, retrieving, updating, and deleting
 * shipping records. It handles shipping information, status tracking, and delivery
 * date management for orders.
 * </p>
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.ShippingCreationRequest;
import com.ecommerce_app.dto.request.ShippingUpdateRequest;
import com.ecommerce_app.dto.response.ShippingResponse;

import java.util.List;
import java.util.UUID;

public interface ShippingService {
    /**
     * Creates a new shipping record in the system.
     *
     * @param shippingCreationRequest The {@link ShippingCreationRequest} containing shipping details
     * @return {@link ShippingResponse} representing the created shipping record
     */
    ShippingResponse createShipping(ShippingCreationRequest shippingCreationRequest);

    /**
     * Updates an existing shipping record.
     *
     * @param id The UUID of the shipping record to update
     * @param shippingUpdateRequest The {@link ShippingUpdateRequest} containing updated shipping details
     * @return {@link ShippingResponse} representing the updated shipping record
     */
    ShippingResponse updateShipping(UUID id, ShippingUpdateRequest shippingUpdateRequest);

    /**
     * Retrieves a shipping record by its unique identifier.
     *
     * @param id The UUID of the shipping record to retrieve
     * @return {@link ShippingResponse} containing the requested shipping details
     */
    ShippingResponse getShippingById(UUID id);

    /**
     * Retrieves a shipping record associated with a specific order.
     *
     * @param orderId The UUID of the order to get shipping for
     * @return {@link ShippingResponse} containing the shipping details for the specified order
     */
    ShippingResponse getShippingByOrderId(UUID orderId);

    /**
     * Retrieves all shipping records in the system.
     *
     * @return A list of {@link ShippingResponse} objects representing all shipping records
     */
    List<ShippingResponse> getAllShippings();

    /**
     * Deletes a shipping record from the system.
     *
     * @param id The UUID of the shipping record to delete
     */
    void deleteShipping(UUID id);

    /**
     * Checks if a shipping record exists for a specific order.
     *
     * @param orderId The UUID of the order to check
     * @return {@code true} if a shipping record exists for the order, {@code false} otherwise
     */
    boolean existsByOrderId(UUID orderId);

    /**
     * Updates the status of an existing shipping record.
     *
     * @param id The UUID of the shipping record to update
     * @param status The new status to set
     * @return {@link ShippingResponse} representing the updated shipping record
     */
    ShippingResponse updateShippingStatus(UUID id, String status);

    /**
     * Updates the tracking information for an existing shipping record.
     *
     * @param id The UUID of the shipping record to update
     * @param trackingNumber The new tracking number
     * @param carrier The shipping carrier
     * @return {@link ShippingResponse} representing the updated shipping record
     */
    ShippingResponse updateTrackingInfo(UUID id, String trackingNumber, String carrier);

    /**
     * Updates the delivery date for an existing shipping record.
     *
     * @param id The UUID of the shipping record to update
     * @param isActual Whether the date is an actual delivery date (true) or an estimated date (false)
     * @return {@link ShippingResponse} representing the updated shipping record
     */
    ShippingResponse updateDeliveryDate(UUID id, boolean isActual);
}