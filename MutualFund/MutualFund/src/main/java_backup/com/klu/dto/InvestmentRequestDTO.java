package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Inbound DTO for placing an investment.
 *
 * Request body example:
 * <pre>
 * {
 *   "userId": 1,
 *   "fundId": 3,
 *   "amount": 10000.00,
 *   "type":   "LUMP_SUM"
 * }
 * </pre>
 *
 * {@code type} is optional — defaults to {@code LUMP_SUM} if omitted.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentRequestDTO {

    /** ID of the investor (User.id) */
    private Long userId;

    /** ID of the fund being invested in (MutualFund.id) */
    private Long fundId;

    /** Amount to invest in INR (e.g. 10000.00) */
    private BigDecimal amount;

    /** Investment type: LUMP_SUM | SIP  (defaults to LUMP_SUM) */
    private String type;
}
