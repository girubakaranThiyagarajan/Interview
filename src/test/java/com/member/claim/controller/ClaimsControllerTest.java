
package com.member.claim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.member.claim.dto.ClaimSubmissionResponse;
import com.member.claim.service.ClaimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClaimsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClaimService claimSubmissionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testSubmitClaim() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ClaimSubmissionResponse submitted = new ClaimSubmissionResponse(1l, "Submitted");
        Mockito.when(claimSubmissionService.submitClaimRequest(any())).thenReturn(submitted);
        this.mockMvc.perform(
                        post("/claims/submit")
                                .headers(headers)
                                .content("{" +
                                        "\"policyHolderId\": 2," + " \"dateOfService\": \"10-12-2022\"," +
                                        "\"categoryId\":4,\n" + " \"billedAmt\": 200\n" +
                                        "}")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    ClaimSubmissionResponse response = objectMapper.readValue(
                            result.getResponse().getContentAsString(), ClaimSubmissionResponse.class);
                });
    }
}
