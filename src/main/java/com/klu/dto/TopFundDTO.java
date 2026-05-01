package com.klu.dto;

import java.math.BigDecimal;

public class TopFundDTO {

    private Long        id;
    private String      fundName;
    private String      category;
    private String      riskLevel;
    private BigDecimal  returns;
    private BigDecimal  nav;
    private BigDecimal  navGrowth;
    private Long        totalInvestors;
    private BigDecimal  totalAum;
    private int         rank;

    public TopFundDTO() {}

public Long getId() { return id; }
    public String getFundName() { return fundName; }
    public String getCategory() { return category; }
    public String getRiskLevel() { return riskLevel; }
    public BigDecimal getReturns() { return returns; }
    public BigDecimal getNav() { return nav; }
    public BigDecimal getNavGrowth() { return navGrowth; }
    public Long getTotalInvestors() { return totalInvestors; }
    public BigDecimal getTotalAum() { return totalAum; }
    public int getRank() { return rank; }

public void setId(Long id) { this.id = id; }
    public void setFundName(String fundName) { this.fundName = fundName; }
    public void setCategory(String category) { this.category = category; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public void setReturns(BigDecimal returns) { this.returns = returns; }
    public void setNav(BigDecimal nav) { this.nav = nav; }
    public void setNavGrowth(BigDecimal navGrowth) { this.navGrowth = navGrowth; }
    public void setTotalInvestors(Long totalInvestors) { this.totalInvestors = totalInvestors; }
    public void setTotalAum(BigDecimal totalAum) { this.totalAum = totalAum; }
    public void setRank(int rank) { this.rank = rank; }

public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String fundName; private String category;
        private String riskLevel; private BigDecimal returns; private BigDecimal nav;
        private BigDecimal navGrowth; private Long totalInvestors;
        private BigDecimal totalAum; private int rank;

        public Builder id(Long v) { this.id = v; return this; }
        public Builder fundName(String v) { this.fundName = v; return this; }
        public Builder category(String v) { this.category = v; return this; }
        public Builder riskLevel(String v) { this.riskLevel = v; return this; }
        public Builder returns(BigDecimal v) { this.returns = v; return this; }
        public Builder nav(BigDecimal v) { this.nav = v; return this; }
        public Builder navGrowth(BigDecimal v) { this.navGrowth = v; return this; }
        public Builder totalInvestors(Long v) { this.totalInvestors = v; return this; }
        public Builder totalAum(BigDecimal v) { this.totalAum = v; return this; }
        public Builder rank(int v) { this.rank = v; return this; }

        public TopFundDTO build() {
            TopFundDTO dto = new TopFundDTO();
            dto.id = id; dto.fundName = fundName; dto.category = category;
            dto.riskLevel = riskLevel; dto.returns = returns; dto.nav = nav;
            dto.navGrowth = navGrowth; dto.totalInvestors = totalInvestors;
            dto.totalAum = totalAum; dto.rank = rank;
            return dto;
        }
    }
}
