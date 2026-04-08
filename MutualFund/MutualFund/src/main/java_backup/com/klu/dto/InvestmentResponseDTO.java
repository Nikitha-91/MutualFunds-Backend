package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Outbound DTO representing a single Investment record.
 *
 * Exposes userId and fundId directly (as requested),
 * plus computed fields (currentValue, gainLoss) for dashboard use.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentResponseDTO {

    /** Investment primary key */
    private Long id;

    /** ID of the investor */
    private Long userId;

    /** Display name of the investor */
    private String userName;

    /** ID of the fund */
    private Long fundId;

    /** Name of the fund */
    private String fundName;

    /** Amount invested in INR */
    private BigDecimal amount;

    /** Units allocated at time of purchase */
    private BigDecimal units;

    /** NAV per unit at time of purchase */
    private BigDecimal purchaseNav;

    /** Date of investment */
    private LocalDate date;

    /** LUMP_SUM | SIP */
    private String type;

    /** ACTIVE | REDEEMED | CANCELLED */
    private String status;

    /** units × currentNav  (computed at query time) */
    private BigDecimal currentValue;

    /** currentValue − amount */
    private BigDecimal gainLoss;

    /** (gainLoss / amount) × 100 */
    private BigDecimal gainLossPercent;
}
