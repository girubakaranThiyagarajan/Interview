package com.member.claim.validator;

import com.member.claim.dto.ClaimProcessRequest;

import java.util.List;

@FunctionalInterface
public interface ClaimProcessValidator {
    List<String> validateClaimsRequest(ClaimProcessRequest claimProcessRequest);
}
