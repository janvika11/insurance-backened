package com.example.insurance.controller;

import com.example.insurance.entity.Policy;
import com.example.insurance.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @PostMapping
    public String createPolicy(@RequestBody Policy policy) {
        return policyService.createPolicy(policy);
    }

    // Get policy by ID
    @GetMapping("/{id}")
    public Policy getPolicy(@PathVariable Long id) {
        return policyService.getPolicyById(id);
    }


    @GetMapping
    public List<Policy> getAllPolicies() {
        return policyService.getAllPolicies();
    }


    @GetMapping("/customer/{customerId}")
    public List<Policy> getCustomerPolicies(@PathVariable Long customerId) {
        return policyService.getPoliciesByCustomerId(customerId);
    }

    @PutMapping("/{id}")
    public String updatePolicy(@PathVariable Long id, @RequestBody Policy policy) {
        return policyService.updatePolicy(id, policy);
    }


    @DeleteMapping("/{id}")
    public String deletePolicy(@PathVariable Long id) {
        return policyService.deletePolicy(id);
    }
}