package com.member.claim.repository;

import com.member.claim.entity.Category;
import com.member.claim.entity.PlanCategory;
import com.member.claim.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanCategoryRepository extends JpaRepository<PlanCategory, Long> {
 PlanCategory findByCategoryAndPlanId(Category category,String planId);
}
