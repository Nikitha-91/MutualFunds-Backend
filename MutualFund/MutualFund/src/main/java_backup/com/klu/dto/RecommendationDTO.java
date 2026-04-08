package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a fund recommendation posted by an ADVISOR.
 *
 * Fields
 * ------
 * fundId      – the fund being recommended
 * fundName    – denormalised name for display
 * advisorId   – the ADVISOR user's ID
 * advisorName – denormalised name for display
 * rationale   – free-text justification / analysis
 * action      – BUY | HOLD | SELL
 * targetNav   – price target the advisor expects the fund to reach
 * date        – date the recommendation was issued
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {

    private Long      fundId;
    private String    fundName;
    private Long      advisorId;
    private String    advisorName;
    private String    rationale;
    private String    action;        // BUY | HOLD | SELL
    private String    riskLevel;
    private String    category;
    private double    returns;
    private LocalDate date;
}
