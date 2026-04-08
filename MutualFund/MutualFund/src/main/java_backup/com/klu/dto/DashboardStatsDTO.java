package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Outbound DTO for dashboard / portfolio summary statistics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    private BigDecimal totalInvested;      // Sum of all active investment amounts
    private BigDecimal currentValue;       // Sum of (units × currentNav) for all active
    private BigDecimal gainLoss;           // currentValue − totalInvested
    private BigDecimal gainLossPercent;    // (gainLoss / totalInvested) × 100
    private Long activeFundsCount;         // Distinct funds with ACTIVE investments
    private Long totalInvestmentsCount;    // Total number of investment records
}
