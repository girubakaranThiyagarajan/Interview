package com.member.claim.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "plan_category")
@Data
public class PlanCategory implements Serializable {
    @Id
    @Column(name = "plan_category_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planCategoryId;
    @Column(name = "plan_Id")
    private String planId;
    @Column(name = "deductible_pct")
    BigDecimal deductiblePct;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_Id", nullable = false)
    private Category category;
}
