package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Monthly investment report for a user or the entire platform.
 *
 * Contains one entry per month, with breakdown by fund category.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportDTO {

    private int    year;
    private String reportScope;   // "USER:{userId}" or "PLATFORM"

    /** One entry per month that has data */
    private List<MonthEntry> months;

    /** Summary totals across the entire report period */
    private BigDecimal totalInvestedInPeriod;
    private long       totalTransactionsInPeriod;
    private long       activeFundsInPeriod;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthEntry {
        private int        month;          // 1–12
        private String     monthName;      // "January", "February", ...
        private BigDecimal totalInvested;  // Sum of amounts in this month
        private long       transactionCount;
        private BigDecimal averageAmount;  // totalInvested / transactionCount
        private BigDecimal cumulativeTotal; // Running total from Jan through this month

        /** Breakdown by fund category within this month */
        private List<CategoryBreakdown> categoryBreakdown;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryBreakdown {
        private String     category;
        private BigDecimal amount;
        private long       transactions;
    }
}
