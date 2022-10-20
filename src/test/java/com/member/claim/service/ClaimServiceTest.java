package com.member.claim.service;

import com.member.claim.dto.ClaimProcessRequest;
import com.member.claim.dto.ClaimSubmissionResponse;
import com.member.claim.entity.Category;
import com.member.claim.entity.Claims;
import com.member.claim.repository.CategoryRepository;
import com.member.claim.repository.ClaimsRepository;
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
public class ClaimServiceTest {

    @Autowired
    private ClaimService claimProcessService;
    @MockBean
    ClaimsRepository claimsRepository;
    @MockBean
    CategoryRepository categoryRepository;

    @Test
    public void testClaimSuccessfullySubmitted() {
        Category category = new Category();
        category.setCategoryId(7l);
        Mockito.when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        Claims claim=new Claims();
        claim.setClaimId(1l);
        Mockito.when(claimsRepository.save(any())).thenReturn(claim);
        ClaimSubmissionResponse claimProcessResponse = claimProcessService.submitClaimRequest(new ClaimProcessRequest());
        Assert.assertNotNull(claimProcessResponse);
        Assert.assertEquals("Claim Submitted",claimProcessResponse.getStatus());
    }
}