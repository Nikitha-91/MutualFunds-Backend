package com.klu.dto;

import java.time.LocalDate;

public class RecommendationDTO {

    private Long      fundId;
    private String    fundName;
    private Long      advisorId;
    private String    advisorName;
    private String    rationale;
    private String    action;
    private String    riskLevel;
    private String    category;
    private double    returns;
    private LocalDate date;

    public RecommendationDTO() {}

public Long getFundId() { return fundId; }
    public String getFundName() { return fundName; }
    public Long getAdvisorId() { return advisorId; }
    public String getAdvisorName() { return advisorName; }
    public String getRationale() { return rationale; }
    public String getAction() { return action; }
    public String getRiskLevel() { return riskLevel; }
    public String getCategory() { return category; }
    public double getReturns() { return returns; }
    public LocalDate getDate() { return date; }

public void setFundId(Long fundId) { this.fundId = fundId; }
    public void setFundName(String fundName) { this.fundName = fundName; }
    public void setAdvisorId(Long advisorId) { this.advisorId = advisorId; }
    public void setAdvisorName(String advisorName) { this.advisorName = advisorName; }
    public void setRationale(String rationale) { this.rationale = rationale; }
    public void setAction(String action) { this.action = action; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public void setCategory(String category) { this.category = category; }
    public void setReturns(double returns) { this.returns = returns; }
    public void setDate(LocalDate date) { this.date = date; }

public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long fundId; private String fundName;
        private Long advisorId; private String advisorName;
        private String rationale; private String action;
        private String riskLevel; private String category;
        private double returns; private LocalDate date;

        public Builder fundId(Long v) { this.fundId = v; return this; }
        public Builder fundName(String v) { this.fundName = v; return this; }
        public Builder advisorId(Long v) { this.advisorId = v; return this; }
        public Builder advisorName(String v) { this.advisorName = v; return this; }
        public Builder rationale(String v) { this.rationale = v; return this; }
        public Builder action(String v) { this.action = v; return this; }
        public Builder riskLevel(String v) { this.riskLevel = v; return this; }
        public Builder category(String v) { this.category = v; return this; }
        public Builder returns(double v) { this.returns = v; return this; }
        public Builder date(LocalDate v) { this.date = v; return this; }

        public RecommendationDTO build() {
            RecommendationDTO dto = new RecommendationDTO();
            dto.fundId = fundId; dto.fundName = fundName; dto.advisorId = advisorId;
            dto.advisorName = advisorName; dto.rationale = rationale; dto.action = action;
            dto.riskLevel = riskLevel; dto.category = category;
            dto.returns = returns; dto.date = date;
            return dto;
        }
    }
}
