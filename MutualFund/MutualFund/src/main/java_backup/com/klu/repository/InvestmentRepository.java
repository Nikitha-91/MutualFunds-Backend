package com.klu.repository;

import com.klu.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * JPA repository for {@link Investment} entities.
 *
 * Provides finders and aggregate queries used by the three core APIs:
 *  1. Invest in fund
 *  2. Get user investments
 *  3. Calculate total investment
 */
@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    // ── By user ────────────────────────────────────────────────────

    /** All investments placed by a specific user (any status). */
    List<Investment> findByUser_Id(Long userId);

    /** All investments for a user filtered by status. */
    List<Investment> findByUser_IdAndStatus(Long userId, Investment.InvestmentStatus status);

    /** Investments for a user in a specific fund. */
    List<Investment> findByUser_IdAndMutualFund_Id(Long userId, Long fundId);

    // ── By fund ────────────────────────────────────────────────────

    /** All investments in a specific fund. */
    List<Investment> findByMutualFund_Id(Long fundId);

    // ── Aggregate: Total investment amount ─────────────────────────

    /**
     * Sum of all ACTIVE investment amounts for a user.
     * Returns {@code null} if the user has no active investments.
     */
    @Query("SELECT SUM(i.amount) FROM Investment i " +
           "WHERE i.user.id = :userId AND i.status = com.klu.entity.Investment.InvestmentStatus.ACTIVE")
    BigDecimal getTotalInvestedAmountByUserId(@Param("userId") Long userId);

    /**
     * Sum of ALL investments for a user regardless of status.
     */
    @Query("SELECT SUM(i.amount) FROM Investment i WHERE i.user.id = :userId")
    BigDecimal getGrandTotalByUserId(@Param("userId") Long userId);

    /**
     * Sum of investments for a user within a date range.
     */
    @Query("SELECT SUM(i.amount) FROM Investment i " +
           "WHERE i.user.id = :userId AND i.date BETWEEN :from AND :to")
    BigDecimal getTotalByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("from")   LocalDate from,
            @Param("to")     LocalDate to);

    // ── Aggregate: Fund diversification ───────────────────────────

    /** Count of distinct funds a user has ACTIVE investments in. */
    @Query("SELECT COUNT(DISTINCT i.mutualFund.id) FROM Investment i " +
           "WHERE i.user.id = :userId AND i.status = com.klu.entity.Investment.InvestmentStatus.ACTIVE")
    Long getDistinctFundsCountByUserId(@Param("userId") Long userId);

    // ── Existence check ────────────────────────────────────────────

    boolean existsByUser_IdAndMutualFund_Id(Long userId, Long fundId);

    /** Total count of all investment transactions for a user. */
    Long countByUser_Id(Long userId);

    // ── Analytics: Top Funds ──────────────────────────────────────

    /**
     * Total AUM (sum of active investment amounts) grouped by fund.
     * Returns rows of [fundId (Long), totalAum (BigDecimal)].
     */
    @Query("SELECT i.mutualFund.id AS fundId, SUM(i.amount) AS totalAum " +
           "FROM Investment i WHERE i.status = com.klu.entity.Investment.InvestmentStatus.ACTIVE " +
           "GROUP BY i.mutualFund.id")
    List<Object[]> getAumByFund();

    /**
     * Distinct investor (user) count per fund for ACTIVE investments.
     * Returns rows of [fundId (Long), investorCount (Long)].
     */
    @Query("SELECT i.mutualFund.id AS fundId, COUNT(DISTINCT i.user.id) AS investorCount " +
           "FROM Investment i WHERE i.status = com.klu.entity.Investment.InvestmentStatus.ACTIVE " +
           "GROUP BY i.mutualFund.id")
    List<Object[]> getInvestorCountByFund();

    // ── Analytics: Monthly Report ─────────────────────────────────

    /**
     * Platform-wide monthly investment totals grouped by month, year, and category.
     * Mapped to {@link MonthlyInvestmentProjection}.
     */
    @Query("SELECT MONTH(i.date) AS month, YEAR(i.date) AS year, " +
           "SUM(i.amount) AS totalAmount, COUNT(i) AS transactionCount, " +
           "i.mutualFund.category AS category " +
           "FROM Investment i " +
           "WHERE YEAR(i.date) = :year " +
           "GROUP BY YEAR(i.date), MONTH(i.date), i.mutualFund.category " +
           "ORDER BY MONTH(i.date) ASC")
    List<MonthlyInvestmentProjection> getMonthlyReport(@Param("year") int year);

    /**
     * User-specific monthly investment totals grouped by month, year, and category.
     */
    @Query("SELECT MONTH(i.date) AS month, YEAR(i.date) AS year, " +
           "SUM(i.amount) AS totalAmount, COUNT(i) AS transactionCount, " +
           "i.mutualFund.category AS category " +
           "FROM Investment i " +
           "WHERE i.user.id = :userId AND YEAR(i.date) = :year " +
           "GROUP BY YEAR(i.date), MONTH(i.date), i.mutualFund.category " +
           "ORDER BY MONTH(i.date) ASC")
    List<MonthlyInvestmentProjection> getMonthlyReportByUser(
            @Param("userId") Long userId,
            @Param("year")   int year);
}
