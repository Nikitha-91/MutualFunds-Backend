package com.klu.dto;

import java.math.BigDecimal;

public class MutualFundRequestDTO {

    private String fundName;
    private String category;
    private String riskLevel;
    private BigDecimal returns;
    private BigDecimal nav;
    private String description;

    public MutualFundRequestDTO() {}
    public MutualFundRequestDTO(String fundName, String category, String riskLevel,
                                BigDecimal returns, BigDecimal nav, String description) {
        this.fundName = fundName;
        this.category = category;
        this.riskLevel = riskLevel;
        this.returns = returns;
        this.nav = nav;
        this.description = description;
    }

    public String getFundName() { return fundName; }
    public void setFundName(String fundName) { this.fundName = fundName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public BigDecimal getReturns() { return returns; }
    public void setReturns(BigDecimal returns) { this.returns = returns; }
    public BigDecimal getNav() { return nav; }
    public void setNav(BigDecimal nav) { this.nav = nav; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
