package com.example.insurance.controller;

import com.example.insurance.entity.Claim;
import com.example.insurance.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/claim")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    // Submit new claim
    @PostMapping
    public String submitClaim(@RequestBody Claim claim) {
        return claimService.submitClaim(claim);
    }

    // Get claim by ID
    @GetMapping("/{id}")
    public Claim getClaim(@PathVariable Long id) {
        return claimService.getClaimById(id);
    }

    // Get all claims
    @GetMapping
    public List<Claim> getAllClaims() {
        return claimService.getAllClaims();
    }

    // Get claims by policy ID
    @GetMapping("/policy/{policyId}")
    public List<Claim> getClaimsByPolicy(@PathVariable Long policyId) {
        return claimService.getClaimsByPolicyId(policyId);
    }

    // Get claims by status
    @GetMapping("/status/{status}")
    public List<Claim> getClaimsByStatus(@PathVariable String status) {
        return claimService.getClaimsByStatus(status);
    }

    // Update claim status
    @PutMapping("/{id}/status")
    public String updateClaimStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        return claimService.updateClaimStatus(id, newStatus);
    }

    // Delete claim
    @DeleteMapping("/{id}")
    public String deleteClaim(@PathVariable Long id) {
        return claimService.deleteClaim(id);
    }

    // Get claim statistics for a policy
    @GetMapping("/stats/{policyId}")
    public String getClaimStats(@PathVariable Long policyId) {
        return claimService.getClaimStats(policyId);
    }
}
