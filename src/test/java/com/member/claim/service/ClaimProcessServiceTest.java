package com.member.claim.service;

import com.member.claim.dto.ClaimProcessRequest;
import com.member.claim.dto.ClaimProcessResponse;
import com.member.claim.dto.ClaimSubmissionResponse;
import com.member.claim.entity.Category;
import com.member.claim.entity.Claims;
import com.member.claim.repository.CategoryRepository;
import com.member.claim.repository.ClaimsRepository;
import com.member.claim.repository.PlanCategoryRepository;
import com.member.claim.repository.PolicyRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClaimProcessServiceTest {

    @Autowired
    private ClaimProcessService claimProcessService;
    @MockBean
    ClaimsRepository claimsRepository;
    @MockBean
    PolicyRepository policyRepository;
    @MockBean
    PlanCategoryRepository planCategoryRepository;

    @Test
    public void serviceReturnsErrorCodeonInvalidClaim() {
        Claims claim=new Claims();
        claim.setClaimId(1l);
        Mockito.when(claimsRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        ClaimProcessResponse claimProcessResponse = claimProcessService.processClaimRequest(1l);
        Assert.assertNotNull(claimProcessResponse);
        Assert.assertEquals("E001",claimProcessResponse.getErrorCode());
    }

}