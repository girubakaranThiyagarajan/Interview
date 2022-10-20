package com.member.claim.service;

import com.member.claim.constant.Constants;
import com.member.claim.dto.ClaimProcessResponse;
import com.member.claim.entity.*;
import com.member.claim.repository.ClaimsRepository;
import com.member.claim.repository.PlanCategoryRepository;
import com.member.claim.repository.PolicyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClaimProcessServiceImpl implements ClaimProcessService {
    private final PolicyRepository policyRepository;
    private final PlanCategoryRepository planCategoryRepository;
    private final ClaimsRepository claimsRepository;
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    public ClaimProcessServiceImpl(PolicyRepository policyRepository, PlanCategoryRepository planCategoryRepository, ClaimsRepository claimsRepository) {
        this.policyRepository = policyRepository;
        this.planCategoryRepository = planCategoryRepository;
        this.claimsRepository = claimsRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ClaimProcessResponse processClaimRequest(Long claimId) {
        ClaimProcessResponse response = new ClaimProcessResponse();
        Optional<Claims> claimOpt = claimsRepository.findById(claimId);
        //TODO Need to use Enum to refactor the error processing
        if (!claimOpt.isPresent()) {
            response.setErrorCode("E001");
            response.setErrorMessage("Invalid Claim Id"+claimId);
            return response;
        }
        Claims submittedClaim = claimOpt.get();
        Category category = submittedClaim.getCategoryId();
        Optional<Policy> policyOptional = policyRepository.findById(submittedClaim.getPolicyHolderId());
        if (!policyOptional.isPresent()) {
            response.setErrorCode("E002");
            response.setErrorMessage("Policy Holder does not exist"+submittedClaim.getPolicyHolderId());
            return response;
        }
        Policy policy = policyOptional.get();
        Plan plan = policy.getPlanId();
        PlanCategory byCategoryAndPlanId = planCategoryRepository.findByCategoryAndPlanId(category, plan.getPlanId());
        BigDecimal billedAmt = submittedClaim.getBilledAmt();
        BigDecimal planIndivDeductible = plan.getAnnualDeductibleIndv();
        BigDecimal indivDeductibleAccumulated = policy.getIndAccumulatedDed();
        BigDecimal famDeductibleAccumulated = policy.getFamAccumulatedDed();
        BigDecimal planPays = BigDecimal.ZERO;
        BigDecimal youPay = BigDecimal.ZERO;
        //If the claim category is Zero deductible or the deductible is already met by the member
        if (byCategoryAndPlanId.getDeductiblePct().compareTo(BigDecimal.ZERO) == 0
                || planIndivDeductible.compareTo(indivDeductibleAccumulated) <= 0) {
            planPays = billedAmt;
            youPay = BigDecimal.ZERO;
            response.setProcessMessage("ANNUAL DECUCTIBLE  (INDIVIDUAL or FAMILY) met- plan pays 100%");
        }
        // If the Deductible is not Met
        if (planIndivDeductible.compareTo(indivDeductibleAccumulated) > 0) {
            planPays = billedAmt.multiply(byCategoryAndPlanId.getDeductiblePct()).divide(ONE_HUNDRED);
            youPay = billedAmt.subtract(planPays);
            response.setProcessMessage("ANNUAL DECUCTIBLE  (INDIVIDUAL or FAMILY) not met- plan pays "
                    + byCategoryAndPlanId.getDeductiblePct() + "%");
        }

        BigDecimal indAccumulatedDed = indivDeductibleAccumulated.add(youPay);
        BigDecimal famAccumulatedDed = famDeductibleAccumulated.add(youPay);
        populateClaimProcessResponse(response, submittedClaim, category, policy, byCategoryAndPlanId,
                billedAmt, planPays, youPay, indAccumulatedDed, famAccumulatedDed);
        policy.setIndAccumulatedDed(indAccumulatedDed);
        policy.setFamAccumulatedDed(famAccumulatedDed);
        // update Family deductible to all respective policy records
        List<Policy> allByPlanId = policyRepository.findAllByPolicyId(policy.getPolicyId());
        if (!CollectionUtils.isEmpty(allByPlanId)) {
            allByPlanId.stream().forEach(t -> {
                t.setFamAccumulatedDed(famAccumulatedDed);
                policyRepository.save(t);
            });
        }
        submittedClaim.setStatus(Constants.PROCESSED);
        claimsRepository.save(submittedClaim);
        log.debug(" The Response is " + response);
        return response;

    }

    private void populateClaimProcessResponse(ClaimProcessResponse response, Claims submittedClaim, Category category,
                                              Policy policy, PlanCategory byCategoryAndPlanId, BigDecimal billedAmt,
                                              BigDecimal planPays, BigDecimal youPay, BigDecimal indAccumulatedDed,
                                              BigDecimal famAccumulatedDed) {
        response.setDateOfService(submittedClaim.getDateOfService());
        response.setBilledAmt(billedAmt);
        response.setPolicyId(policy.getPolicyId());
        response.setPolicyHolderId(policy.getPolicyHolderId());
        response.setCategory(category.getCategory());
        response.setSubCategory(category.getSubCategory());
        response.setPolicyHolderPays(youPay);
        response.setPlanPays(planPays);
        response.setRuleUsed(byCategoryAndPlanId.getDeductiblePct() + "% AFTER DEDUCTIBLE");
        response.setIndAccumulatedDed(indAccumulatedDed);
        response.setFamAccumulatedDed(famAccumulatedDed);
    }

}
