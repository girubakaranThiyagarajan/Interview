package com.member.claim.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "claims")
@Data
public class Claims implements Serializable {
    @Id
    @Column(name = "claim_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long claimId;
    @Column(name = "policy_holder_id")
    private Long policyHolderId;
    @Column(name = "date_of_service")
    private Date dateOfService;
    @OneToOne
    @JoinColumn(name="category_Id", nullable=false)
    private Category categoryId;
    @Column(name = "billed_amt")
    private BigDecimal billedAmt;
    @Column(name = "status")
    private String status;
}
