package com.klu.dto;

import java.math.BigDecimal;

public class MutualFundResponseDTO {

    private Long id;
    private String fundName;
    private String category;
    private String riskLevel;
    private BigDecimal returns;
    private BigDecimal nav;
    private String description;
    private boolean active;

    public MutualFundResponseDTO() {}

public Long getId() { return id; }
    public String getFundName() { return fundName; }
    public String getCategory() { return category; }
    public String getRiskLevel() { return riskLevel; }
    public BigDecimal getReturns() { return returns; }
    public BigDecimal getNav() { return nav; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }

public void setId(Long id) { this.id = id; }
    public void setFundName(String fundName) { this.fundName = fundName; }
    public void setCategory(String category) { this.category = category; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public void setReturns(BigDecimal returns) { this.returns = returns; }
    public void setNav(BigDecimal nav) { this.nav = nav; }
    public void setDescription(String description) { this.description = description; }
    public void setActive(boolean active) { this.active = active; }

public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id;
        private String fundName;
        private String category;
        private String riskLevel;
        private BigDecimal returns;
        private BigDecimal nav;
        private String description;
        private boolean active;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder fundName(String fundName) { this.fundName = fundName; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder riskLevel(String riskLevel) { this.riskLevel = riskLevel; return this; }
        public Builder returns(BigDecimal returns) { this.returns = returns; return this; }
        public Builder nav(BigDecimal nav) { this.nav = nav; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder active(boolean active) { this.active = active; return this; }

        public MutualFundResponseDTO build() {
            MutualFundResponseDTO dto = new MutualFundResponseDTO();
            dto.id = id; dto.fundName = fundName; dto.category = category;
            dto.riskLevel = riskLevel; dto.returns = returns; dto.nav = nav;
            dto.description = description; dto.active = active;
            return dto;
        }
    }
}
