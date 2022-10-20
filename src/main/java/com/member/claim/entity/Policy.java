package com.member.claim.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "policy")
@Data
public class Policy implements Serializable {
    @Id
    @Column(name = "policy_holder_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long policyHolderId;
    @Column(name = "policy_id")
    private String policyId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @OneToOne
    @JoinColumn(name="plan_id", nullable=false)
    private Plan planId;
    @Column(name = "coverage_start_date")
    Date coverageStartDate;
    @Column(name = "coverage_end_date")
    Date coverageEndDate;
    @Column(name = "indv_accumulated_ded")
    BigDecimal indAccumulatedDed;
    @Column(name = "family_accumulated_ded")
    BigDecimal famAccumulatedDed;
}
