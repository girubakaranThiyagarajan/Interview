package com.member.claim.batch;


import com.member.claim.dto.ClaimProcessResponse;
import com.member.claim.dto.ClaimsBatchRequest;
import com.member.claim.service.ClaimProcessService;
import org.springframework.batch.item.ItemProcessor;

public class ClaimProcessor implements ItemProcessor<ClaimsBatchRequest, ClaimProcessResponse> {

    private final ClaimProcessService claimProcessService;

    public ClaimProcessor(ClaimProcessService claimProcessService) {
        this.claimProcessService = claimProcessService;
    }

    @Override
    public ClaimProcessResponse process(ClaimsBatchRequest request) {
        return claimProcessService.processClaimRequest(request.getClaimId());
    }
}
