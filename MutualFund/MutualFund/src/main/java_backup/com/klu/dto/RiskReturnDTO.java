package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents one bucket in the Risk vs Return comparison matrix.
 *
 * Each bucket = one risk level (LOW / MODERATE / HIGH / VERY_HIGH)
 * with aggregated return statistics and individual fund breakdowns.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskReturnDTO {

    private String     riskLevel;       // LOW | MODERATE | HIGH | VERY_HIGH
    private long       fundCount;       // Number of active funds in this risk bucket
    private BigDecimal averageReturns;  // Average annualised return for this bucket
    private BigDecimal minReturns;      // Lowest return in this bucket
    private BigDecimal maxReturns;      // Highest return in this bucket
    private BigDecimal medianReturns;   // Median return (computed in-memory)

    /** Individual funds in this risk bucket, sorted by returns descending */
    private List<FundSummary> funds;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FundSummary {
        private Long       id;
        private String     fundName;
        private BigDecimal returns;
        private BigDecimal nav;
    }
}
