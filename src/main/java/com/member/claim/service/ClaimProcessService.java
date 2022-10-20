package com.member.claim.service;

import com.member.claim.dto.ClaimProcessRequest;
import com.member.claim.dto.ClaimProcessResponse;
import com.member.claim.entity.Claims;

public interface ClaimProcessService {
    ClaimProcessResponse processClaimRequest(Long claimId);
}
