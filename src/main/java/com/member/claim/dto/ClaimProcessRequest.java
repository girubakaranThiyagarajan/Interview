package com.member.claim.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ClaimProcessRequest {
    private Long claimId;
    private Long policyHolderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    private Date dateOfService;
    private Long categoryId;
    private BigDecimal billedAmt;
}
