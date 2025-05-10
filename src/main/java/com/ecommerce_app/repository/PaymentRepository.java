package com.ecommerce_app.repository;

import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentNumber(String paymentNumber);

    Optional<Payment> findByOrder_Id(Order orderId);

    List<Payment> findByStatus(Payment.PaymentStatus status);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByOrder_IdAndStatus(Order orderId, Payment.PaymentStatus status);
}
