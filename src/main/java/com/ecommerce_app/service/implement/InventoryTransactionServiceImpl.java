package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.InventoryTransactionCreationRequest;
import com.ecommerce_app.dto.request.InventoryTransactionUpdateRequest;
import com.ecommerce_app.dto.response.InventoryTransactionResponse;
import com.ecommerce_app.entity.Inventory;
import com.ecommerce_app.entity.InventoryTransaction;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.InventoryTransactionMapper;
import com.ecommerce_app.repository.InventoryRepository;
import com.ecommerce_app.repository.InventoryTransactionRepository;
import com.ecommerce_app.repository.UserRepository;
import com.ecommerce_app.service.interfaces.InventoryTransactionService;
import lombok.RequiredArgsConstructor;
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
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    private final InventoryTransactionRepository transactionRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final InventoryTransactionMapper transactionMapper;

    @Override
    @Transactional
    public InventoryTransactionResponse createTransaction(InventoryTransactionCreationRequest request) {
        // Find the inventory
        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory id" + request.getInventoryId()));

        // Find the user if createdBy is provided
        User user = null;
        if (request.getCreatedBy() != null) {
            user = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("User id" + request.getCreatedBy()));
        }

        // Create the transaction entity
        InventoryTransaction transaction = transactionMapper.toEntity(request, inventory, user);

        // Update inventory based on transaction type
        updateInventoryBasedOnTransaction(inventory, request.getTransactionType(), request.getQuantity());

        // Save the transaction
        InventoryTransaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    public InventoryTransactionResponse getTransactionById(Long id) {
        InventoryTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryTransaction id" + id));

        return transactionMapper.toResponse(transaction);
    }

    @Override
    public List<InventoryTransactionResponse> getTransactionsByInventoryId(UUID inventoryId) {
        List<InventoryTransaction> transactions = transactionRepository.findByInventoryId(inventoryId);

        return transactions.stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<InventoryTransactionResponse> getTransactionsByInventoryId(UUID inventoryId, Pageable pageable) {
        Page<InventoryTransaction> transactions = transactionRepository.findByInventoryId(inventoryId, pageable);

        return transactions.map(transactionMapper::toResponse);
    }

    @Override
    public List<InventoryTransactionResponse> getTransactionsByType(UUID inventoryId, String transactionType) {
        List<InventoryTransaction> transactions = transactionRepository.findByInventoryIdAndTransactionType(
                inventoryId, transactionType);

        return transactions.stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryTransactionResponse> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        List<InventoryTransaction> transactions = transactionRepository.findByCreatedAtBetween(start, end);

        return transactions.stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryTransactionResponse> getTransactionsByReference(String reference) {
        List<InventoryTransaction> transactions = transactionRepository.findByReference(reference);

        return transactions.stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InventoryTransactionResponse updateTransaction(Long id, InventoryTransactionUpdateRequest request) {
        InventoryTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryTransaction id" + id));

        // Store the old values before update
        String oldTransactionType = transaction.getTransactionType();
        Integer oldQuantity = transaction.getQuantity();

        // Update the transaction entity
        transactionMapper.updateEntity(transaction, request);

        // If transaction type or quantity has changed, adjust the inventory accordingly
        if (!oldTransactionType.equals(request.getTransactionType()) || !oldQuantity.equals(request.getQuantity())) {
            // Reverse the effects of the old transaction
            updateInventoryBasedOnTransaction(transaction.getInventory(), oldTransactionType, -oldQuantity);

            // Apply the effects of the new transaction
            updateInventoryBasedOnTransaction(transaction.getInventory(), request.getTransactionType(), request.getQuantity());
        }

        // Save the updated transaction
        InventoryTransaction updatedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        InventoryTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryTransaction id" + id));

        // Reverse the effects of the transaction on inventory
        updateInventoryBasedOnTransaction(transaction.getInventory(), transaction.getTransactionType(), -transaction.getQuantity());

        // Delete the transaction
        transactionRepository.deleteById(id);
    }

    private void updateInventoryBasedOnTransaction(Inventory inventory, String transactionType, Integer quantity) {
        switch (transactionType) {
            case "STOCK_IN":
                inventory.setStockQuantity(inventory.getStockQuantity() + quantity);
                break;
            case "STOCK_OUT":
                if (inventory.getStockQuantity() < quantity && quantity > 0) {
                    throw new IllegalStateException("Not enough stock available for STOCK_OUT transaction");
                }
                inventory.setStockQuantity(inventory.getStockQuantity() - quantity);
                break;
            case "ADJUSTMENT":
                // Direct adjustment to the stock quantity
                inventory.setStockQuantity(inventory.getStockQuantity() + quantity);
                break;
            case "RESERVATION":
                if ((inventory.getStockQuantity() - inventory.getReservedQuantity()) < quantity && quantity > 0) {
                    throw new IllegalStateException("Not enough available stock for RESERVATION transaction");
                }
                inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
                break;
            case "RELEASE_RESERVATION":
                if (inventory.getReservedQuantity() < quantity && quantity > 0) {
                    throw new IllegalStateException("Not enough reserved stock for RELEASE_RESERVATION transaction");
                }
                inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }

        // Save the updated inventory
        inventoryRepository.save(inventory);
    }
}