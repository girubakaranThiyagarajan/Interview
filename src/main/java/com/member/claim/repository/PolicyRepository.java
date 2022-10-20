package com.member.claim.repository;

import com.member.claim.entity.Plan;
import com.member.claim.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
 List<Policy> findAllByPlanId(Plan planId);
 List<Policy> findAllByPolicyId(String policyId);
}
