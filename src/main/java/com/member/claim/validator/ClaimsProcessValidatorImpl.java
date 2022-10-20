package com.member.claim.validator;

import com.member.claim.dto.ClaimProcessRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ClaimsProcessValidatorImpl implements ClaimProcessValidator {
    @Override
    public List<String> validateClaimsRequest(ClaimProcessRequest request) {
        List<String> violations=new ArrayList<>();
        if(!request.getDateOfService().before(new Date()))
            violations.add("Date Of Service must be in the past");
        if(request.getBilledAmt()==null)
            violations.add("Billed Amount must present");
        return violations;
    }
}