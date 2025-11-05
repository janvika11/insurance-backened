package com.example.insurance.service;

import com.example.insurance.entity.Payment;
import com.example.insurance.repository.CustomerRepository;
import com.example.insurance.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public String processPayment(Payment payment) {
        // Validation 1: Check if customer exists
        if (!customerRepository.existsById(payment.getCustomerId())) {
            return "Error: Customer with ID " + payment.getCustomerId() + " not found";
        }

        // Validation 2: Amount must be positive
        if (payment.getAmount() <= 0) {
            return "Error: Payment amount must be greater than 0";
        }

        // Validation 3: Payment method must be valid
        String method = payment.getPaymentMethod();
        if (!method.equals("CREDIT_CARD") && !method.equals("DEBIT_CARD") &&
                !method.equals("BANK_TRANSFER") && !method.equals("UPI")) {
            return "Error: Invalid payment method. Must be CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, or UPI";
        }

        // Generate unique transaction ID
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        payment.setTransactionId(transactionId);

        // Simulate payment processing (in real app, this would call payment gateway)
        boolean paymentSuccessful = simulatePaymentGateway(payment);

        if (paymentSuccessful) {
            payment.setStatus("SUCCESS");
            Payment savedPayment = paymentRepository.save(payment);
            return "Payment processed successfully! Transaction ID: " + savedPayment.getTransactionId() +
                    " | Amount: $" + savedPayment.getAmount();
        } else {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            return "Error: Payment processing failed. Please try again.";
        }
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByCustomerId(customerId);
    }

    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    public Payment getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    public String refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);

        if (payment == null) {
            return "Error: Payment with ID " + paymentId + " not found";
        }

        if (!payment.getStatus().equals("SUCCESS")) {
            return "Error: Can only refund successful payments";
        }

        // Simulate refund process
        payment.setStatus("REFUNDED");
        paymentRepository.save(payment);

        return "Payment refunded successfully. Transaction ID: " + payment.getTransactionId();
    }

    // Simulate payment gateway (returns success/failure randomly for demo)
    // In real application, this would integrate with Stripe, PayPal, Razorpay, etc.
    private boolean simulatePaymentGateway(Payment payment) {
        // For demo purposes, we'll always return success
        // In production, you'd call actual payment gateway API here
        return true;
    }

    // Get total payments for a customer
    public String getCustomerPaymentStats(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            return "Error: Customer not found";
        }

        List<Payment> payments = paymentRepository.findByCustomerId(customerId);

        double totalPaid = payments.stream()
                .filter(p -> p.getStatus().equals("SUCCESS"))
                .mapToDouble(Payment::getAmount)
                .sum();

        long successfulPayments = payments.stream()
                .filter(p -> p.getStatus().equals("SUCCESS"))
                .count();

        long failedPayments = payments.stream()
                .filter(p -> p.getStatus().equals("FAILED"))
                .count();

        return "Payment Statistics for Customer " + customerId + ":\n" +
                "Total Paid: $" + totalPaid + "\n" +
                "Successful Payments: " + successfulPayments + "\n" +
                "Failed Payments: " + failedPayments;
    }
}