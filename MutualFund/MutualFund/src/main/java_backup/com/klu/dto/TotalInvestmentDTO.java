package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Outbound DTO for the total investment summary of a user.
 * Returned by the "Calculate total investment" API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalInvestmentDTO {

    /** User ID */
    private Long userId;

    /** Sum of all ACTIVE investment amounts */
    private BigDecimal totalActiveAmount;

    /** Sum of ALL investments (any status) */
    private BigDecimal grandTotalAmount;

    /** Current market value of ACTIVE portfolio (units × currentNav) */
    private BigDecimal currentPortfolioValue;

    /** Overall gain/loss: currentPortfolioValue − totalActiveAmount */
    private BigDecimal overallGainLoss;

    /** Overall gain/loss as a percentage */
    private BigDecimal overallGainLossPercent;

    /** Number of distinct funds invested in (ACTIVE only) */
    private Long distinctFundsCount;

    /** Total count of investment transactions */
    private Long totalTransactions;
}
