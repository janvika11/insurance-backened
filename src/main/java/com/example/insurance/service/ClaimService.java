package com.example.insurance.service;

import com.example.insurance.entity.Claim;
import com.example.insurance.entity.Policy;
import com.example.insurance.repository.ClaimRepository;
import com.example.insurance.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimService {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private PolicyRepository policyRepository;

    public String submitClaim(Claim claim) {
        // Validation 1: Check if policy exists
        Policy policy = policyRepository.findById(claim.getPolicyId()).orElse(null);
        if (policy == null) {
            return "Error: Policy with ID " + claim.getPolicyId() + " not found";
        }

        // Validation 2: Check if claim amount exceeds policy limit
        if (claim.getClaimAmount() > policy.getPolicyLimit()) {
            return "Error: Claim amount $" + claim.getClaimAmount() +
                    " exceeds policy limit $" + policy.getPolicyLimit();
        }

        // Validation 3: Claim amount must be positive
        if (claim.getClaimAmount() <= 0) {
            return "Error: Claim amount must be greater than 0";
        }

        // Validation 4: Incident date cannot be in the future
        if (claim.getIncidentDate().isAfter(java.time.LocalDate.now())) {
            return "Error: Incident date cannot be in the future";
        }

        // Save claim with pending status
        Claim savedClaim = claimRepository.save(claim);

        return "Claim submitted successfully with ID: " + savedClaim.getId() +
                " | Status: " + savedClaim.getStatus();
    }

    public Claim getClaimById(Long id) {
        return claimRepository.findById(id).orElse(null);
    }

    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    public List<Claim> getClaimsByPolicyId(Long policyId) {
        return claimRepository.findByPolicyId(policyId);
    }

    public List<Claim> getClaimsByStatus(String status) {
        return claimRepository.findByStatus(status);
    }

    public String updateClaimStatus(Long id, String newStatus) {
        Claim claim = claimRepository.findById(id).orElse(null);

        if (claim == null) {
            return "Error: Claim with ID " + id + " not found";
        }

        // Validate status
        if (!newStatus.equals("PENDING") && !newStatus.equals("APPROVED") && !newStatus.equals("REJECTED")) {
            return "Error: Invalid status. Must be PENDING, APPROVED, or REJECTED";
        }

        claim.setStatus(newStatus);
        claimRepository.save(claim);

        return "Claim status updated to: " + newStatus;
    }

    public String deleteClaim(Long id) {
        if (!claimRepository.existsById(id)) {
            return "Error: Claim with ID " + id + " not found";
        }

        claimRepository.deleteById(id);
        return "Claim deleted successfully";
    }

    // Get claim statistics for a policy
    public String getClaimStats(Long policyId) {
        if (!policyRepository.existsById(policyId)) {
            return "Error: Policy not found";
        }

        Long pendingCount = claimRepository.countByPolicyIdAndStatus(policyId, "PENDING");
        Long approvedCount = claimRepository.countByPolicyIdAndStatus(policyId, "APPROVED");
        Long rejectedCount = claimRepository.countByPolicyIdAndStatus(policyId, "REJECTED");

        return "Claim Statistics for Policy " + policyId + ":\n" +
                "Pending: " + pendingCount + "\n" +
                "Approved: " + approvedCount + "\n" +
                "Rejected: " + rejectedCount;
    }
}
