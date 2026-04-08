package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a top-performing mutual fund entry in the analytics response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopFundDTO {

    private Long        id;
    private String      fundName;
    private String      category;
    private String      riskLevel;
    private BigDecimal  returns;       // Annualised return %
    private BigDecimal  nav;
    private BigDecimal  navGrowth;     // (currentNAV - purchaseNAV) / purchaseNAV * 100  — approx
    private Long        totalInvestors; // Number of users who invested in this fund
    private BigDecimal  totalAum;       // Total amount invested across all users (platform AUM)
    private int         rank;           // 1 = best
}
