package com.klu.repository;

import com.klu.entity.MutualFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * JPA repository for {@link MutualFund} entities.
 *
 * Provides ready-made finders for the common filter + search patterns
 * used by the REST API without writing any JPQL manually.
 */
@Repository
public interface MutualFundRepository extends JpaRepository<MutualFund, Long> {

    // ── Active-only listings ────────────────────────────────────────
    /** All funds that are not soft-deleted. */
    List<MutualFund> findByActiveTrue();

    // ── Category filter ─────────────────────────────────────────────
    List<MutualFund> findByCategoryIgnoreCase(String category);

    List<MutualFund> findByCategoryIgnoreCaseAndActiveTrue(String category);

    // ── Risk level filter ───────────────────────────────────────────
    List<MutualFund> findByRiskLevel(MutualFund.RiskLevel riskLevel);

    List<MutualFund> findByRiskLevelAndActiveTrue(MutualFund.RiskLevel riskLevel);

    // ── Name search ─────────────────────────────────────────────────
    List<MutualFund> findByFundNameContainingIgnoreCase(String fundName);

    // ── Returns filter ──────────────────────────────────────────────
    /** Funds with annualised returns ≥ a minimum threshold. */
    @Query("SELECT f FROM MutualFund f WHERE f.returns >= :minReturns AND f.active = true")
    List<MutualFund> findByMinReturns(@Param("minReturns") BigDecimal minReturns);

    // ── Existence check ─────────────────────────────────────────────
    boolean existsByFundNameIgnoreCase(String fundName);
}
