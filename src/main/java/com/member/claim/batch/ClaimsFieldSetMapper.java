package com.member.claim.batch;

import com.member.claim.dto.ClaimsBatchRequest;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class ClaimsFieldSetMapper implements FieldSetMapper<ClaimsBatchRequest> {

    @Override
    public ClaimsBatchRequest mapFieldSet(FieldSet fieldSet) {
        return new ClaimsBatchRequest(fieldSet.readLong("claimId"),
                fieldSet.readLong("policyHolderId"),
                fieldSet.readDate("dateOfService", "yyyy-MM-dd"),
                fieldSet.readLong("categoryId"),
                fieldSet.readBigDecimal("billedAmt"),
                fieldSet.readString("status"));
    }
}