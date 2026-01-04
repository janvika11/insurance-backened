package com.example.insurance.service;

import com.example.insurance.entity.Policy;
import com.example.insurance.repository.CustomerRepository;
import com.example.insurance.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public String createPolicy(Policy policy) {
        // Validation 1: Checks if customer exists
        if (!customerRepository.existsById(policy.getCustomerId())) {
            return "Error: Customer with ID " + policy.getCustomerId() + " not found";
        }

        // Validation 2: Checks if customer already has this policy type
        if (policyRepository.existsByCustomerIdAndPolicyType(
                policy.getCustomerId(), policy.getPolicyType())) {
            return "Error: Customer already has a " + policy.getPolicyType() + " policy";
        }

        // Validation 3: Premium must be positive
        if (policy.getPremium() <= 0) {
            return "Error: Premium must be greater than 0";
        }

        // Set policy limit based on policy type (business logic)
        policy.setPolicyLimit(calculatePolicyLimit(policy.getPolicyType(), policy.getPremium()));

        // Save policy
        Policy savedPolicy = policyRepository.save(policy);

        return "Policy created successfully with ID: " + savedPolicy.getId() +
                " | Coverage Limit: $" + savedPolicy.getPolicyLimit();
    }

    public Policy getPolicyById(Long id) {
        return policyRepository.findById(id).orElse(null);
    }

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    public List<Policy> getPoliciesByCustomerId(Long customerId) {
        return policyRepository.findByCustomerId(customerId);
    }

    public String updatePolicy(Long id, Policy updatedPolicy) {
        Policy existingPolicy = policyRepository.findById(id).orElse(null);

        if (existingPolicy == null) {
            return "Error: Policy with ID " + id + " not found";
        }

        // Update only allowed fields
        if (updatedPolicy.getPremium() != null && updatedPolicy.getPremium() > 0) {
            existingPolicy.setPremium(updatedPolicy.getPremium());
            existingPolicy.setPolicyLimit(calculatePolicyLimit(
                    existingPolicy.getPolicyType(), updatedPolicy.getPremium()));
        }

        policyRepository.save(existingPolicy);
        return "Policy updated successfully";
    }

    public String deletePolicy(Long id) {
        if (!policyRepository.existsById(id)) {
            return "Error: Policy with ID " + id + " not found";
        }

        policyRepository.deleteById(id);
        return "Policy deleted successfully";
    }

    // Business Logic: Calculate coverage limit based on policy type
    private Double calculatePolicyLimit(String policyType, Double premium) {
        switch (policyType.toUpperCase()) {
            case "HEALTH":
                return premium * 10;  // Health: 10x premium
            case "AUTO":
                return premium * 15;  // Auto: 15x premium
            case "LIFE":
                return premium * 20;  // Life: 20x premium
            case "HOME":
                return premium * 12;  // Home: 12x premium
            default:
                return premium * 5;   // Default: 5x premium
        }
    }
}