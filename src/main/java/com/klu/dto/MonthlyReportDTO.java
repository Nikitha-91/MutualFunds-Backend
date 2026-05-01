package com.klu.dto;

import java.math.BigDecimal;
import java.util.List;

public class MonthlyReportDTO {

    private int    year;
    private String reportScope;
    private List<MonthEntry> months;
    private BigDecimal totalInvestedInPeriod;
    private long       totalTransactionsInPeriod;
    private long       activeFundsInPeriod;

    public MonthlyReportDTO() {}

public int getYear() { return year; }
    public String getReportScope() { return reportScope; }
    public List<MonthEntry> getMonths() { return months; }
    public BigDecimal getTotalInvestedInPeriod() { return totalInvestedInPeriod; }
    public long getTotalTransactionsInPeriod() { return totalTransactionsInPeriod; }
    public long getActiveFundsInPeriod() { return activeFundsInPeriod; }

public void setYear(int year) { this.year = year; }
    public void setReportScope(String reportScope) { this.reportScope = reportScope; }
    public void setMonths(List<MonthEntry> months) { this.months = months; }
    public void setTotalInvestedInPeriod(BigDecimal v) { this.totalInvestedInPeriod = v; }
    public void setTotalTransactionsInPeriod(long v) { this.totalTransactionsInPeriod = v; }
    public void setActiveFundsInPeriod(long v) { this.activeFundsInPeriod = v; }

public static Builder builder() { return new Builder(); }
    public static class Builder {
        private int year; private String reportScope;
        private List<MonthEntry> months;
        private BigDecimal totalInvestedInPeriod;
        private long totalTransactionsInPeriod;
        private long activeFundsInPeriod;

        public Builder year(int v) { this.year = v; return this; }
        public Builder reportScope(String v) { this.reportScope = v; return this; }
        public Builder months(List<MonthEntry> v) { this.months = v; return this; }
        public Builder totalInvestedInPeriod(BigDecimal v) { this.totalInvestedInPeriod = v; return this; }
        public Builder totalTransactionsInPeriod(long v) { this.totalTransactionsInPeriod = v; return this; }
        public Builder activeFundsInPeriod(long v) { this.activeFundsInPeriod = v; return this; }

        public MonthlyReportDTO build() {
            MonthlyReportDTO dto = new MonthlyReportDTO();
            dto.year = year; dto.reportScope = reportScope; dto.months = months;
            dto.totalInvestedInPeriod = totalInvestedInPeriod;
            dto.totalTransactionsInPeriod = totalTransactionsInPeriod;
            dto.activeFundsInPeriod = activeFundsInPeriod;
            return dto;
        }
    }

public static class MonthEntry {
        private int        month;
        private String     monthName;
        private BigDecimal totalInvested;
        private long       transactionCount;
        private BigDecimal averageAmount;
        private BigDecimal cumulativeTotal;
        private List<CategoryBreakdown> categoryBreakdown;

        public MonthEntry() {}

        public int getMonth() { return month; }
        public String getMonthName() { return monthName; }
        public BigDecimal getTotalInvested() { return totalInvested; }
        public long getTransactionCount() { return transactionCount; }
        public BigDecimal getAverageAmount() { return averageAmount; }
        public BigDecimal getCumulativeTotal() { return cumulativeTotal; }
        public List<CategoryBreakdown> getCategoryBreakdown() { return categoryBreakdown; }

        public void setMonth(int month) { this.month = month; }
        public void setMonthName(String monthName) { this.monthName = monthName; }
        public void setTotalInvested(BigDecimal v) { this.totalInvested = v; }
        public void setTransactionCount(long v) { this.transactionCount = v; }
        public void setAverageAmount(BigDecimal v) { this.averageAmount = v; }
        public void setCumulativeTotal(BigDecimal v) { this.cumulativeTotal = v; }
        public void setCategoryBreakdown(List<CategoryBreakdown> v) { this.categoryBreakdown = v; }

        public static MonthEntryBuilder builder() { return new MonthEntryBuilder(); }
        public static class MonthEntryBuilder {
            private int month; private String monthName;
            private BigDecimal totalInvested; private long transactionCount;
            private BigDecimal averageAmount; private BigDecimal cumulativeTotal;
            private List<CategoryBreakdown> categoryBreakdown;

            public MonthEntryBuilder month(int v) { this.month = v; return this; }
            public MonthEntryBuilder monthName(String v) { this.monthName = v; return this; }
            public MonthEntryBuilder totalInvested(BigDecimal v) { this.totalInvested = v; return this; }
            public MonthEntryBuilder transactionCount(long v) { this.transactionCount = v; return this; }
            public MonthEntryBuilder averageAmount(BigDecimal v) { this.averageAmount = v; return this; }
            public MonthEntryBuilder cumulativeTotal(BigDecimal v) { this.cumulativeTotal = v; return this; }
            public MonthEntryBuilder categoryBreakdown(List<CategoryBreakdown> v) { this.categoryBreakdown = v; return this; }

            public MonthEntry build() {
                MonthEntry e = new MonthEntry();
                e.month = month; e.monthName = monthName; e.totalInvested = totalInvested;
                e.transactionCount = transactionCount; e.averageAmount = averageAmount;
                e.cumulativeTotal = cumulativeTotal; e.categoryBreakdown = categoryBreakdown;
                return e;
            }
        }
    }

public static class CategoryBreakdown {
        private String     category;
        private BigDecimal amount;
        private long       transactions;

        public CategoryBreakdown() {}

        public String getCategory() { return category; }
        public BigDecimal getAmount() { return amount; }
        public long getTransactions() { return transactions; }
        public void setCategory(String category) { this.category = category; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public void setTransactions(long transactions) { this.transactions = transactions; }

        public static CategoryBreakdownBuilder builder() { return new CategoryBreakdownBuilder(); }
        public static class CategoryBreakdownBuilder {
            private String category; private BigDecimal amount; private long transactions;

            public CategoryBreakdownBuilder category(String v) { this.category = v; return this; }
            public CategoryBreakdownBuilder amount(BigDecimal v) { this.amount = v; return this; }
            public CategoryBreakdownBuilder transactions(long v) { this.transactions = v; return this; }

            public CategoryBreakdown build() {
                CategoryBreakdown cb = new CategoryBreakdown();
                cb.category = category; cb.amount = amount; cb.transactions = transactions;
                return cb;
            }
        }
    }
}
