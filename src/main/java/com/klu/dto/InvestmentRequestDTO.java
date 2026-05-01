package com.klu.dto;

import java.math.BigDecimal;

public class InvestmentRequestDTO {

    private Long userId;
    private Long fundId;
    private BigDecimal amount;
    private String type;

    public InvestmentRequestDTO() {}
    public InvestmentRequestDTO(Long userId, Long fundId, BigDecimal amount, String type) {
        this.userId = userId;
        this.fundId = fundId;
        this.amount = amount;
        this.type = type;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getFundId() { return fundId; }
    public void setFundId(Long fundId) { this.fundId = fundId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
