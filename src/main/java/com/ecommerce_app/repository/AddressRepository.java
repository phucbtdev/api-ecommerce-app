package com.ecommerce_app.repository;

import com.ecommerce_app.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findByUserId(UUID userId);

    Optional<Address> findByUserIdAndIsDefaultTrue(UUID userId);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.addressType IN ('SHIPPING', 'BOTH')")
    List<Address> findShippingAddressesByUserId(@Param("userId") UUID userId);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.addressType IN ('BILLING', 'BOTH')")
    List<Address> findBillingAddressesByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId AND a.id <> :addressId")
    void unsetDefaultAddressesExcept(@Param("userId") UUID userId, @Param("addressId") UUID addressId);

    boolean existsByUserIdAndIsDefaultTrue(UUID userId);
}
