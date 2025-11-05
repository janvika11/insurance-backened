package com.example.insurance.controller;

import com.example.insurance.entity.Payment;
import com.example.insurance.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Process new payment
    @PostMapping
    public String processPayment(@RequestBody Payment payment) {
        return paymentService.processPayment(payment);
    }

    // Get payment by ID
    @GetMapping("/{id}")
    public Payment getPayment(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    // Get all payments
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // Get payments by customer ID
    @GetMapping("/customer/{customerId}")
    public List<Payment> getCustomerPayments(@PathVariable Long customerId) {
        return paymentService.getPaymentsByCustomerId(customerId);
    }

    // Get payments by status
    @GetMapping("/status/{status}")
    public List<Payment> getPaymentsByStatus(@PathVariable String status) {
        return paymentService.getPaymentsByStatus(status);
    }

    // Get payment by transaction ID
    @GetMapping("/transaction/{transactionId}")
    public Payment getPaymentByTransactionId(@PathVariable String transactionId) {
        return paymentService.getPaymentByTransactionId(transactionId);
    }

    // Refund payment
    @PostMapping("/{id}/refund")
    public String refundPayment(@PathVariable Long id) {
        return paymentService.refundPayment(id);
    }

    // Get customer payment statistics
    @GetMapping("/stats/{customerId}")
    public String getPaymentStats(@PathVariable Long customerId) {
        return paymentService.getCustomerPaymentStats(customerId);
    }
}
