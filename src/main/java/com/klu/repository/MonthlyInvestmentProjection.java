package com.klu.repository;

import java.math.BigDecimal;

public interface MonthlyInvestmentProjection {

Integer getMonth();

Integer getYear();

BigDecimal getTotalAmount();

Long getTransactionCount();

String getCategory();
}
