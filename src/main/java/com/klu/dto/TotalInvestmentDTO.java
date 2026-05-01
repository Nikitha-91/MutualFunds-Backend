package com.klu.dto;

import java.math.BigDecimal;

public class TotalInvestmentDTO {

    private Long userId;
    private BigDecimal totalActiveAmount;
    private BigDecimal grandTotalAmount;
    private BigDecimal currentPortfolioValue;
    private BigDecimal overallGainLoss;
    private BigDecimal overallGainLossPercent;
    private Long distinctFundsCount;
    private Long totalTransactions;

    public TotalInvestmentDTO() {}

public Long getUserId() { return userId; }
    public BigDecimal getTotalActiveAmount() { return totalActiveAmount; }
    public BigDecimal getGrandTotalAmount() { return grandTotalAmount; }
    public BigDecimal getCurrentPortfolioValue() { return currentPortfolioValue; }
    public BigDecimal getOverallGainLoss() { return overallGainLoss; }
    public BigDecimal getOverallGainLossPercent() { return overallGainLossPercent; }
    public Long getDistinctFundsCount() { return distinctFundsCount; }
    public Long getTotalTransactions() { return totalTransactions; }

public void setUserId(Long userId) { this.userId = userId; }
    public void setTotalActiveAmount(BigDecimal v) { this.totalActiveAmount = v; }
    public void setGrandTotalAmount(BigDecimal v) { this.grandTotalAmount = v; }
    public void setCurrentPortfolioValue(BigDecimal v) { this.currentPortfolioValue = v; }
    public void setOverallGainLoss(BigDecimal v) { this.overallGainLoss = v; }
    public void setOverallGainLossPercent(BigDecimal v) { this.overallGainLossPercent = v; }
    public void setDistinctFundsCount(Long v) { this.distinctFundsCount = v; }
    public void setTotalTransactions(Long v) { this.totalTransactions = v; }

public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long userId;
        private BigDecimal totalActiveAmount, grandTotalAmount, currentPortfolioValue;
        private BigDecimal overallGainLoss, overallGainLossPercent;
        private Long distinctFundsCount, totalTransactions;

        public Builder userId(Long v) { this.userId = v; return this; }
        public Builder totalActiveAmount(BigDecimal v) { this.totalActiveAmount = v; return this; }
        public Builder grandTotalAmount(BigDecimal v) { this.grandTotalAmount = v; return this; }
        public Builder currentPortfolioValue(BigDecimal v) { this.currentPortfolioValue = v; return this; }
        public Builder overallGainLoss(BigDecimal v) { this.overallGainLoss = v; return this; }
        public Builder overallGainLossPercent(BigDecimal v) { this.overallGainLossPercent = v; return this; }
        public Builder distinctFundsCount(Long v) { this.distinctFundsCount = v; return this; }
        public Builder totalTransactions(Long v) { this.totalTransactions = v; return this; }

        public TotalInvestmentDTO build() {
            TotalInvestmentDTO dto = new TotalInvestmentDTO();
            dto.userId = userId; dto.totalActiveAmount = totalActiveAmount;
            dto.grandTotalAmount = grandTotalAmount; dto.currentPortfolioValue = currentPortfolioValue;
            dto.overallGainLoss = overallGainLoss; dto.overallGainLossPercent = overallGainLossPercent;
            dto.distinctFundsCount = distinctFundsCount; dto.totalTransactions = totalTransactions;
            return dto;
        }
    }
}
