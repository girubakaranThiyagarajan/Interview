package com.member.claim.controller;

import com.member.claim.dto.ClaimProcessRequest;
import com.member.claim.dto.ClaimProcessResponse;
import com.member.claim.dto.ClaimSubmissionResponse;
import com.member.claim.exception.InvalidClaimException;
import com.member.claim.service.ClaimProcessService;
import com.member.claim.service.ClaimService;
import com.member.claim.validator.ClaimProcessValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/claims")
public class ClaimsController {
    private final ClaimService claimSubmissionService;
    private final ClaimProcessService claimProcessService;

    ClaimProcessValidator validator = (ClaimProcessRequest request) -> {
        List<String> violations = new ArrayList<>();
        if (request.getDateOfService()!=null && !request.getDateOfService().before(new Date()))
            violations.add("Date Of Service must be in the past");
        if (request.getBilledAmt() == null)
            violations.add("Billed Amount cannot be empty");
        return violations;
    };

    public ClaimsController(ClaimService claimProcessService, ClaimProcessService claimProcessService1) {
        this.claimSubmissionService = claimProcessService;
        this.claimProcessService = claimProcessService1;
    }

    @PostMapping("/submit")
    public ResponseEntity<ClaimSubmissionResponse> submitClaim(@RequestBody ClaimProcessRequest claimProcessRequest) {
        List<String> validationMsgs = validator.validateClaimsRequest(claimProcessRequest);
        if (!CollectionUtils.isEmpty(validationMsgs))
            throw new InvalidClaimException("Invalid Claim Request", validationMsgs);
        ClaimSubmissionResponse claimSubmissionResponse = claimSubmissionService.submitClaimRequest(claimProcessRequest);
        return new ResponseEntity<>(claimSubmissionResponse, HttpStatus.OK);
    }

    @PostMapping("/process")
    public ResponseEntity<ClaimProcessResponse> processClaim(@RequestParam Long claimId) {
        ClaimProcessResponse claimProcessResponse = claimProcessService.processClaimRequest(claimId);
        return new ResponseEntity<>(claimProcessResponse, HttpStatus.OK);
    }
}
