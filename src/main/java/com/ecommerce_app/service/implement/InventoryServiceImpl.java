package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.InventoryCreationRequest;
import com.ecommerce_app.dto.request.InventoryUpdateRequest;
import com.ecommerce_app.dto.response.InventoryResponse;
import com.ecommerce_app.entity.Inventory;
import com.ecommerce_app.entity.ProductVariant;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.InventoryMapper;
import com.ecommerce_app.repository.InventoryRepository;
import com.ecommerce_app.repository.ProductVariantRepository;
import com.ecommerce_app.service.interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public InventoryResponse createInventory(InventoryCreationRequest request) {
        // Check if the product variant exists
        ProductVariant productVariant = productVariantRepository.findById(request.getProductVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("ProductVariant id" + request.getProductVariantId()));

        // Check if inventory already exists for this product variant
        if (inventoryRepository.existsByProductVariantId(request.getProductVariantId())) {
            throw new IllegalStateException("Inventory already exists for this product variant");
        }

        // Map request to entity
        Inventory inventory = inventoryMapper.toEntity(request, productVariant);

        // Save the inventory
        Inventory savedInventory = inventoryRepository.save(inventory);

        // Return the response
        return inventoryMapper.toResponse(savedInventory);
    }

    @Override
    public InventoryResponse getInventoryById(UUID id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory id" + id));

        return inventoryMapper.toResponse(inventory);
    }

    @Override
    public InventoryResponse getInventoryByProductVariantId(UUID productVariantId) {
        Inventory inventory = inventoryRepository.findByProductVariantId(productVariantId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory productVariantId" +productVariantId));

        return inventoryMapper.toResponse(inventory);
    }

    @Override
    public InventoryResponse getInventoryBySku(String sku) {
        Inventory inventory = inventoryRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory sku" + sku));

        return inventoryMapper.toResponse(inventory);
    }

    @Override
    public InventoryResponse getInventoryByProductVariantSku(String sku) {
        Inventory inventory = inventoryRepository.findByProductVariantSku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory productVariantSku" + sku));

        return inventoryMapper.toResponse(inventory);
    }

    @Override
    public List<InventoryResponse> getAllInventories() {
        return inventoryRepository.findAll().stream()
                .map(inventoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<InventoryResponse> getAllInventories(Pageable pageable) {
        return inventoryRepository.findAll(pageable)
                .map(inventoryMapper::toResponse);
    }

    @Override
    @Transactional
    public InventoryResponse updateInventory(UUID id, InventoryUpdateRequest request) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory id" + id));

        // Update the entity with the request values
        inventoryMapper.updateEntity(inventory, request);

        // Save the updated inventory
        Inventory updatedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(updatedInventory);
    }

    @Override
    @Transactional
    public void deleteInventory(UUID id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventory id" + id);
        }

        inventoryRepository.deleteById(id);
    }

    @Override
    public InventoryResponse adjustStockQuantity(UUID id, Integer quantity) {
        return null;
    }

    @Override
    @Transactional
    public InventoryResponse updateStockQuantity(UUID id, Integer stockQuantity) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory id" + id));

        inventory.setStockQuantity(stockQuantity);
        Inventory updatedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(updatedInventory);
    }

    @Override
    @Transactional
    public InventoryResponse reserveStock(UUID id, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory id" + id));

        // Calculate available quantity
        int availableQuantity = inventory.getStockQuantity() - inventory.getReservedQuantity();

        // Check if we have enough stock
        if (availableQuantity < quantity) {
            throw new IllegalStateException("Not enough available stock to reserve. Available: " + availableQuantity + ", Requested: " + quantity);
        }

        // Update reserved quantity
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        Inventory updatedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(updatedInventory);
    }

    @Override
    @Transactional
    public InventoryResponse releaseReservedStock(UUID id, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory id" + id));

        // Check if we have enough reserved stock
        if (inventory.getReservedQuantity() < quantity) {
            throw new IllegalStateException("Not enough reserved stock to release. Reserved: " + inventory.getReservedQuantity() + ", Requested: " + quantity);
        }

        // Update reserved quantity
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        Inventory updatedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(updatedInventory);
    }

    @Override
    @Transactional
    public InventoryResponse commitReservedStock(UUID id, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory id" + id));

        // Check if we have enough reserved stock
        if (inventory.getReservedQuantity() < quantity) {
            throw new IllegalStateException("Not enough reserved stock to commit. Reserved: " + inventory.getReservedQuantity() + ", Requested: " + quantity);
        }

        // Update reserved and stock quantities
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setStockQuantity(inventory.getStockQuantity() - quantity);
        Inventory updatedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(updatedInventory);
    }
}