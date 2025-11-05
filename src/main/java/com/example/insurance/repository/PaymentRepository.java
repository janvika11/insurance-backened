package com.example.insurance.repository;

import com.example.insurance.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByCustomerId(Long customerId);
    List<Payment> findByStatus(String status);
    Payment findByTransactionId(String transactionId);
}