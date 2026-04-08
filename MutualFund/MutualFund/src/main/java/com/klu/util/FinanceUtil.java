package com.klu.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility helpers for common financial calculations
 * used across the service layer.
 */
public final class FinanceUtil {

    private FinanceUtil() { /* utility class */ }

    /**
     * Calculate units purchased given an amount and NAV.
     *
     * @param amount invested amount (INR)
     * @param nav    Net Asset Value per unit
     * @return units allocated, rounded to 4 decimal places
     */
    public static BigDecimal calculateUnits(BigDecimal amount, BigDecimal nav) {
        if (nav == null || nav.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("NAV cannot be zero");
        }
        return amount.divide(nav, 4, RoundingMode.HALF_UP);
    }

    /**
     * Calculate simple percentage gain/loss.
     *
     * @param invested     original invested amount
     * @param currentValue current market value
     * @return gain/loss as percentage, rounded to 2 decimal places
     */
    public static BigDecimal gainLossPercent(BigDecimal invested, BigDecimal currentValue) {
        if (invested == null || invested.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return currentValue.subtract(invested)
                .divide(invested, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Round a BigDecimal to the given number of decimal places.
     */
    public static BigDecimal round(BigDecimal value, int scale) {
        return value.setScale(scale, RoundingMode.HALF_UP);
    }
}
