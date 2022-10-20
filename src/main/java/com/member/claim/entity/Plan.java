package com.member.claim.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "Plan")
@Data
public class Plan implements Serializable {
    @Id
    @Column(name = "plan_id")
    private String planId;
    @Column(name = "plan_Name")
    String planName;
    @Column(name = "coverage_Type")
    String coverageType;
    @Column(name = "estimated_Premium")
    BigDecimal estimated_Premium;
    @Column(name = "annual_deductible_indv")
    BigDecimal annualDeductibleIndv;
    @Column(name = "annual_deductible_fam")
    BigDecimal annualDeductibleFamily;
}
