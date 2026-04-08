package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Inbound DTO for creating or updating a Mutual Fund.
 *
 * All fields except {@code description} are mandatory.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MutualFundRequestDTO {

    /** Full name of the fund, e.g. "Axis Bluechip Fund" */
    private String fundName;

    /**
     * Category of the fund.
     * Accepted values: EQUITY | DEBT | HYBRID | LIQUID | ELSS | INDEX
     */
    private String category;

    /**
     * Risk level.
     * Accepted values: LOW | MODERATE | HIGH | VERY_HIGH
     */
    private String riskLevel;

    /**
     * Annualised return percentage, e.g. 12.50 means 12.50 %
     */
    private BigDecimal returns;

    /**
     * Net Asset Value — current price per unit in INR.
     */
    private BigDecimal nav;

    /**
     * Optional description of the fund's investment objective / strategy.
     */
    private String description;
}
