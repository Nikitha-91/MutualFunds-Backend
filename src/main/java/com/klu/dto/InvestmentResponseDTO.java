package com.klu.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvestmentResponseDTO {

    private Long id;
    private Long userId;
    private String userName;
    private Long fundId;
    private String fundName;
    private BigDecimal amount;
    private BigDecimal units;
    private BigDecimal purchaseNav;
    private LocalDate date;
    private String type;
    private String status;
    private BigDecimal currentValue;
    private BigDecimal gainLoss;
    private BigDecimal gainLossPercent;

    public InvestmentResponseDTO() {}

public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public Long getFundId() { return fundId; }
    public String getFundName() { return fundName; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getUnits() { return units; }
    public BigDecimal getPurchaseNav() { return purchaseNav; }
    public LocalDate getDate() { return date; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public BigDecimal getGainLoss() { return gainLoss; }
    public BigDecimal getGainLossPercent() { return gainLossPercent; }

public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setFundId(Long fundId) { this.fundId = fundId; }
    public void setFundName(String fundName) { this.fundName = fundName; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setUnits(BigDecimal units) { this.units = units; }
    public void setPurchaseNav(BigDecimal purchaseNav) { this.purchaseNav = purchaseNav; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setType(String type) { this.type = type; }
    public void setStatus(String status) { this.status = status; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    public void setGainLoss(BigDecimal gainLoss) { this.gainLoss = gainLoss; }
    public void setGainLossPercent(BigDecimal gainLossPercent) { this.gainLossPercent = gainLossPercent; }

public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id;
        private Long userId;
        private String userName;
        private Long fundId;
        private String fundName;
        private BigDecimal amount;
        private BigDecimal units;
        private BigDecimal purchaseNav;
        private LocalDate date;
        private String type;
        private String status;
        private BigDecimal currentValue;
        private BigDecimal gainLoss;
        private BigDecimal gainLossPercent;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder userName(String userName) { this.userName = userName; return this; }
        public Builder fundId(Long fundId) { this.fundId = fundId; return this; }
        public Builder fundName(String fundName) { this.fundName = fundName; return this; }
        public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder units(BigDecimal units) { this.units = units; return this; }
        public Builder purchaseNav(BigDecimal purchaseNav) { this.purchaseNav = purchaseNav; return this; }
        public Builder date(LocalDate date) { this.date = date; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder currentValue(BigDecimal currentValue) { this.currentValue = currentValue; return this; }
        public Builder gainLoss(BigDecimal gainLoss) { this.gainLoss = gainLoss; return this; }
        public Builder gainLossPercent(BigDecimal gainLossPercent) { this.gainLossPercent = gainLossPercent; return this; }

        public InvestmentResponseDTO build() {
            InvestmentResponseDTO dto = new InvestmentResponseDTO();
            dto.id = id; dto.userId = userId; dto.userName = userName;
            dto.fundId = fundId; dto.fundName = fundName; dto.amount = amount;
            dto.units = units; dto.purchaseNav = purchaseNav; dto.date = date;
            dto.type = type; dto.status = status; dto.currentValue = currentValue;
            dto.gainLoss = gainLoss; dto.gainLossPercent = gainLossPercent;
            return dto;
        }
    }
}
