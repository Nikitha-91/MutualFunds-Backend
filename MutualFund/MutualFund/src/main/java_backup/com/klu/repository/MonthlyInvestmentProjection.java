package com.klu.repository;

import java.math.BigDecimal;

/**
 * Projection interface for the monthly investment report query.
 *
 * Spring Data JPA maps each row of:
 *   SELECT MONTH(i.date), YEAR(i.date), SUM(i.amount), COUNT(i),
 *          f.category
 *   FROM investments i JOIN mutual_funds f ...
 *   GROUP BY MONTH, YEAR, category
 *
 * to one instance of this interface.
 */
public interface MonthlyInvestmentProjection {

    /** Calendar month number (1 = January … 12 = December) */
    Integer getMonth();

    /** Calendar year */
    Integer getYear();

    /** Total invested amount in this month + category */
    BigDecimal getTotalAmount();

    /** Number of transactions in this month + category */
    Long getTransactionCount();

    /** Fund category associated with these transactions */
    String getCategory();
}
