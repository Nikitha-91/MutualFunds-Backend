package com.klu.repository;

import com.klu.entity.MutualFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MutualFundRepository extends JpaRepository<MutualFund, Long> {

List<MutualFund> findByActiveTrue();

List<MutualFund> findByCategoryIgnoreCase(String category);

    List<MutualFund> findByCategoryIgnoreCaseAndActiveTrue(String category);

List<MutualFund> findByRiskLevel(MutualFund.RiskLevel riskLevel);

    List<MutualFund> findByRiskLevelAndActiveTrue(MutualFund.RiskLevel riskLevel);

List<MutualFund> findByFundNameContainingIgnoreCase(String fundName);

@Query("SELECT f FROM MutualFund f WHERE f.returns >= :minReturns AND f.active = true")
    List<MutualFund> findByMinReturns(@Param("minReturns") BigDecimal minReturns);

boolean existsByFundNameIgnoreCase(String fundName);
}
