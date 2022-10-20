package com.member.claim.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "Category")
@Data
public class Category implements Serializable {
    @Id
    @Column(name = "category_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @Column(name = "category")
    String category;
    @Column(name = "sub_category")
    String subCategory;
}
