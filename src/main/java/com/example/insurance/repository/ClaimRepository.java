package com.example.insurance.repository;

import com.example.insurance.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByPolicyId(Long policyId);
    List<Claim> findByStatus(String status);
    Long countByPolicyIdAndStatus(Long policyId, String status);
}