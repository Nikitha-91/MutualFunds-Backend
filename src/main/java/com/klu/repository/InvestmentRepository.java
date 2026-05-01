package com.klu.repository;

import com.klu.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

List<Investment> findByUser_Id(Long userId);

List<Investment> findByUser_IdAndStatus(Long userId, Investment.InvestmentStatus status);

List<Investment> findByUser_IdAndMutualFund_Id(Long userId, Long fundId);

List<Investment> findByMutualFund_Id(Long fundId);

@Query("SELECT SUM(i.amount) FROM Investment i " +
           "WHERE i.user.id = :userId AND i.status = com.klu.entity.Investment.InvestmentStatus.ACTIVE")
    BigDecimal getTotalInvestedAmountByUserId(@Param("userId") Long userId);

@Query("SELECT SUM(i.amount) FROM Investment i WHERE i.user.id = :userId")
    BigDecimal getGrandTotalByUserId(@Param("userId") Long userId);

@Query("SELECT SUM(i.amount) FROM Investment i " +
           "WHERE i.user.id = :userId AND i.date BETWEEN :from AND :to")
    BigDecimal getTotalByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("from")   LocalDate from,
            @Param("to")     LocalDate to);

@Query("SELECT COUNT(DISTINCT i.mutualFund.id) FROM Investment i " +
           "WHERE i.user.id = :userId AND i.status = com.klu.entity.Investment.InvestmentStatus.ACTIVE")
    Long getDistinctFundsCountByUserId(@Param("userId") Long userId);

boolean existsByUser_IdAndMutualFund_Id(Long userId, Long fundId);

Long countByUser_Id(Long userId);

@Query("SELECT i.mutualFund.id AS fundId, SUM(i.amount) AS totalAum " +
           "FROM Investment i WHERE i.status = com.klu.entity.Investment.InvestmentStatus.ACTIVE " +
           "GROUP BY i.mutualFund.id")
    List<Object[]> getAumByFund();

@Query("SELECT i.mutualFund.id AS fundId, COUNT(DISTINCT i.user.id) AS investorCount " +
           "FROM Investment i WHERE i.status = com.klu.entity.Investment.InvestmentStatus.ACTIVE " +
           "GROUP BY i.mutualFund.id")
    List<Object[]> getInvestorCountByFund();

@Query("SELECT MONTH(i.date) AS month, YEAR(i.date) AS year, " +
           "SUM(i.amount) AS totalAmount, COUNT(i) AS transactionCount, " +
           "i.mutualFund.category AS category " +
           "FROM Investment i " +
           "WHERE YEAR(i.date) = :year " +
           "GROUP BY YEAR(i.date), MONTH(i.date), i.mutualFund.category " +
           "ORDER BY MONTH(i.date) ASC")
    List<MonthlyInvestmentProjection> getMonthlyReport(@Param("year") int year);

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
