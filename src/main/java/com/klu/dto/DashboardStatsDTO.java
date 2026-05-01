package com.klu.dto;

import java.math.BigDecimal;

public class DashboardStatsDTO {

    private BigDecimal totalInvested;
    private BigDecimal currentValue;
    private BigDecimal gainLoss;
    private BigDecimal gainLossPercent;
    private Long activeFundsCount;
    private Long totalInvestmentsCount;

    public DashboardStatsDTO() {}

public BigDecimal getTotalInvested() { return totalInvested; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public BigDecimal getGainLoss() { return gainLoss; }
    public BigDecimal getGainLossPercent() { return gainLossPercent; }
    public Long getActiveFundsCount() { return activeFundsCount; }
    public Long getTotalInvestmentsCount() { return totalInvestmentsCount; }

public void setTotalInvested(BigDecimal totalInvested) { this.totalInvested = totalInvested; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    public void setGainLoss(BigDecimal gainLoss) { this.gainLoss = gainLoss; }
    public void setGainLossPercent(BigDecimal gainLossPercent) { this.gainLossPercent = gainLossPercent; }
    public void setActiveFundsCount(Long activeFundsCount) { this.activeFundsCount = activeFundsCount; }
    public void setTotalInvestmentsCount(Long totalInvestmentsCount) { this.totalInvestmentsCount = totalInvestmentsCount; }

public static Builder builder() { return new Builder(); }
    public static class Builder {
        private BigDecimal totalInvested;
        private BigDecimal currentValue;
        private BigDecimal gainLoss;
        private BigDecimal gainLossPercent;
        private Long activeFundsCount;
        private Long totalInvestmentsCount;

        public Builder totalInvested(BigDecimal v) { this.totalInvested = v; return this; }
        public Builder currentValue(BigDecimal v) { this.currentValue = v; return this; }
        public Builder gainLoss(BigDecimal v) { this.gainLoss = v; return this; }
        public Builder gainLossPercent(BigDecimal v) { this.gainLossPercent = v; return this; }
        public Builder activeFundsCount(Long v) { this.activeFundsCount = v; return this; }
        public Builder totalInvestmentsCount(Long v) { this.totalInvestmentsCount = v; return this; }

        public DashboardStatsDTO build() {
            DashboardStatsDTO dto = new DashboardStatsDTO();
            dto.totalInvested = totalInvested; dto.currentValue = currentValue;
            dto.gainLoss = gainLoss; dto.gainLossPercent = gainLossPercent;
            dto.activeFundsCount = activeFundsCount; dto.totalInvestmentsCount = totalInvestmentsCount;
            return dto;
        }
    }
}
