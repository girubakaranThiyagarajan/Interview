package com.member.claim.service;

import com.member.claim.dto.ClaimProcessRequest;
import com.member.claim.dto.ClaimProcessResponse;
import com.member.claim.dto.ClaimSubmissionResponse;
import com.member.claim.entity.Category;
import com.member.claim.entity.Claims;
import com.member.claim.repository.CategoryRepository;
import com.member.claim.repository.ClaimsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.member.claim.constant.Constants.CLAIM_SUBMITTED;
import static com.member.claim.constant.Constants.SUBMITTED;

@Service
@Slf4j
public class ClaimServiceImpl implements ClaimService {

    private final ClaimsRepository claimsRepository;
    private final CategoryRepository categoryRepository;

    public ClaimServiceImpl(ClaimsRepository claimsRepository, CategoryRepository categoryRepository) {
        this.claimsRepository = claimsRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW )
    public ClaimSubmissionResponse submitClaimRequest(ClaimProcessRequest claimProcessRequest) {
        Claims clms = getClaims(claimProcessRequest);
        Claims submittedClaim = claimsRepository.save(clms);
        log.debug(" Claim Submitted For Processing: " + submittedClaim);
        return new ClaimSubmissionResponse(submittedClaim.getClaimId(), CLAIM_SUBMITTED);
    }

    private Claims getClaims(ClaimProcessRequest claimProcessRequest) {
        Claims clms = new Claims();
        Optional<Category> byId = categoryRepository.findById(claimProcessRequest.getCategoryId());
        clms.setBilledAmt(claimProcessRequest.getBilledAmt());
        clms.setCategoryId(byId.get());
        clms.setDateOfService(claimProcessRequest.getDateOfService());
        clms.setPolicyHolderId(claimProcessRequest.getPolicyHolderId());
        clms.setStatus(SUBMITTED);
        return clms;
    }
}
