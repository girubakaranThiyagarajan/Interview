package com.member.claim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimProcessResponse {
    private String policyId;
    private Long policyHolderId;
    private Date dateOfService;
    private String category;
    private String subCategory;
    private BigDecimal billedAmt;
    private BigDecimal policyHolderPays;
    private BigDecimal planPays;
    private String ruleUsed;
    private BigDecimal indAccumulatedDed;
    private BigDecimal famAccumulatedDed;
    private String errorCode;
    private String errorMessage;
    private String processMessage;
}
