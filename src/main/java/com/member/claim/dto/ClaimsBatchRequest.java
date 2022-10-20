package com.member.claim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class ClaimsBatchRequest {
    private Long claimId;
    private Long policyHolderId;
    private Date dateOfService;
    private Long categoryId;
    private BigDecimal billedAmt;
    private String status;
}
