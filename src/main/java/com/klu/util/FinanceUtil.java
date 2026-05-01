package com.klu.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FinanceUtil {

    private FinanceUtil() {  }

public static BigDecimal calculateUnits(BigDecimal amount, BigDecimal nav) {
        if (nav == null || nav.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("NAV cannot be zero");
        }
        return amount.divide(nav, 4, RoundingMode.HALF_UP);
    }

public static BigDecimal gainLossPercent(BigDecimal invested, BigDecimal currentValue) {
        if (invested == null || invested.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return currentValue.subtract(invested)
                .divide(invested, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

public static BigDecimal round(BigDecimal value, int scale) {
        return value.setScale(scale, RoundingMode.HALF_UP);
    }
}
