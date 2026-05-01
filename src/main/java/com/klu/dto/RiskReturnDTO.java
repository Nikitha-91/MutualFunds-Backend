package com.klu.dto;

import java.math.BigDecimal;
import java.util.List;

public class RiskReturnDTO {

    private String     riskLevel;
    private long       fundCount;
    private BigDecimal averageReturns;
    private BigDecimal minReturns;
    private BigDecimal maxReturns;
    private BigDecimal medianReturns;
    private List<FundSummary> funds;

    public RiskReturnDTO() {}

public String getRiskLevel() { return riskLevel; }
    public long getFundCount() { return fundCount; }
    public BigDecimal getAverageReturns() { return averageReturns; }
    public BigDecimal getMinReturns() { return minReturns; }
    public BigDecimal getMaxReturns() { return maxReturns; }
    public BigDecimal getMedianReturns() { return medianReturns; }
    public List<FundSummary> getFunds() { return funds; }

public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public void setFundCount(long fundCount) { this.fundCount = fundCount; }
    public void setAverageReturns(BigDecimal averageReturns) { this.averageReturns = averageReturns; }
    public void setMinReturns(BigDecimal minReturns) { this.minReturns = minReturns; }
    public void setMaxReturns(BigDecimal maxReturns) { this.maxReturns = maxReturns; }
    public void setMedianReturns(BigDecimal medianReturns) { this.medianReturns = medianReturns; }
    public void setFunds(List<FundSummary> funds) { this.funds = funds; }

public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String riskLevel; private long fundCount;
        private BigDecimal averageReturns; private BigDecimal minReturns;
        private BigDecimal maxReturns; private BigDecimal medianReturns;
        private List<FundSummary> funds;

        public Builder riskLevel(String v) { this.riskLevel = v; return this; }
        public Builder fundCount(long v) { this.fundCount = v; return this; }
        public Builder averageReturns(BigDecimal v) { this.averageReturns = v; return this; }
        public Builder minReturns(BigDecimal v) { this.minReturns = v; return this; }
        public Builder maxReturns(BigDecimal v) { this.maxReturns = v; return this; }
        public Builder medianReturns(BigDecimal v) { this.medianReturns = v; return this; }
        public Builder funds(List<FundSummary> v) { this.funds = v; return this; }

        public RiskReturnDTO build() {
            RiskReturnDTO dto = new RiskReturnDTO();
            dto.riskLevel = riskLevel; dto.fundCount = fundCount;
            dto.averageReturns = averageReturns; dto.minReturns = minReturns;
            dto.maxReturns = maxReturns; dto.medianReturns = medianReturns;
            dto.funds = funds;
            return dto;
        }
    }

public static class FundSummary {
        private Long       id;
        private String     fundName;
        private BigDecimal returns;
        private BigDecimal nav;

        public FundSummary() {}

        public Long getId() { return id; }
        public String getFundName() { return fundName; }
        public BigDecimal getReturns() { return returns; }
        public BigDecimal getNav() { return nav; }
        public void setId(Long id) { this.id = id; }
        public void setFundName(String fundName) { this.fundName = fundName; }
        public void setReturns(BigDecimal returns) { this.returns = returns; }
        public void setNav(BigDecimal nav) { this.nav = nav; }

        public static FundSummaryBuilder builder() { return new FundSummaryBuilder(); }
        public static class FundSummaryBuilder {
            private Long id; private String fundName;
            private BigDecimal returns; private BigDecimal nav;

            public FundSummaryBuilder id(Long v) { this.id = v; return this; }
            public FundSummaryBuilder fundName(String v) { this.fundName = v; return this; }
            public FundSummaryBuilder returns(BigDecimal v) { this.returns = v; return this; }
            public FundSummaryBuilder nav(BigDecimal v) { this.nav = v; return this; }

            public FundSummary build() {
                FundSummary fs = new FundSummary();
                fs.id = id; fs.fundName = fundName; fs.returns = returns; fs.nav = nav;
                return fs;
            }
        }
    }
}
