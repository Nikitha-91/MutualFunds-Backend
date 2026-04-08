package com.klu.service;

import com.klu.dto.RecommendationDTO;
import com.klu.entity.MutualFund;
import com.klu.entity.User;
import com.klu.exception.BadRequestException;
import com.klu.exception.ResourceNotFoundException;
import com.klu.repository.MutualFundRepository;
import com.klu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for ADVISOR recommendation operations.
 *
 * Advisors can:
 *  1. Get fund recommendations by category / risk / minimum-return threshold.
 *  2. Create an ad-hoc recommendation for a specific fund (BUY / HOLD / SELL).
 *  3. Browse top BUY opportunities (high-return, active funds).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final MutualFundRepository fundRepository;
    private final UserRepository       userRepository;

    // ================================================================
    //  1. TOP BUY OPPORTUNITIES
    // ================================================================

    /**
     * Returns active funds sorted by returns descending, limited to {@code limit}.
     * These are the advisor's "top buy" candidates.
     *
     * @param limit max results (1–50)
     * @return list of RecommendationDTOs with action = BUY
     */
    @Transactional(readOnly = true)
    public List<RecommendationDTO> getTopBuyOpportunities(Long advisorId, int limit) {
        User advisor = resolveAdvisor(advisorId);

        return fundRepository.findByActiveTrue().stream()
                .sorted(Comparator.comparingDouble(
                        f -> -f.getReturns().doubleValue()))
                .limit(limit)
                .map(f -> toDTO(f, advisor, "BUY",
                        "High annualised return fund suitable for growth-oriented investors."))
                .collect(Collectors.toList());
    }

    // ================================================================
    //  2. RECOMMENDATIONS BY RISK LEVEL
    // ================================================================

    /**
     * Returns fund recommendations filtered to a specific risk level.
     * Action is inferred: LOW → HOLD, MODERATE → BUY, HIGH/VERY_HIGH → SELL (caution).
     *
     * @param advisorId the ADVISOR user's ID
     * @param riskLevel one of LOW | MODERATE | HIGH | VERY_HIGH
     * @return recommendations for that risk bucket
     */
    @Transactional(readOnly = true)
    public List<RecommendationDTO> getRecommendationsByRisk(Long advisorId, String riskLevel) {
        User advisor = resolveAdvisor(advisorId);

        MutualFund.RiskLevel level;
        try {
            level = MutualFund.RiskLevel.valueOf(riskLevel.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    "Invalid risk level '" + riskLevel + "'. Use LOW | MODERATE | HIGH | VERY_HIGH");
        }

        return fundRepository.findByRiskLevelAndActiveTrue(level).stream()
                .sorted(Comparator.comparingDouble(f -> -f.getReturns().doubleValue()))
                .map(f -> toDTO(f, advisor, inferAction(level),
                        "Fund recommended based on " + level.name() + " risk profile."))
                .collect(Collectors.toList());
    }

    // ================================================================
    //  3. RECOMMENDATIONS BY CATEGORY
    // ================================================================

    /**
     * Returns fund recommendations filtered to a specific category
     * (EQUITY, DEBT, HYBRID, LIQUID, ELSS, INDEX).
     *
     * @param advisorId the ADVISOR user's ID
     * @param category  fund category (case-insensitive)
     * @return recommendations for that category
     */
    @Transactional(readOnly = true)
    public List<RecommendationDTO> getRecommendationsByCategory(Long advisorId, String category) {
        User advisor = resolveAdvisor(advisorId);

        return fundRepository.findByCategoryIgnoreCaseAndActiveTrue(category).stream()
                .sorted(Comparator.comparingDouble(f -> -f.getReturns().doubleValue()))
                .map(f -> toDTO(f, advisor, "BUY",
                        "Strong " + category.toUpperCase() + " category fund."))
                .collect(Collectors.toList());
    }

    // ================================================================
    //  4. CUSTOM RECOMMENDATION FOR A SPECIFIC FUND
    // ================================================================

    /**
     * Creates a custom recommendation for a specific fund by the advisor.
     *
     * @param advisorId  ID of the ADVISOR posting the recommendation
     * @param fundId     ID of the fund being recommended
     * @param action     BUY | HOLD | SELL
     * @param rationale  advisor's free-text rationale
     * @return the recommendation DTO
     */
    @Transactional(readOnly = true)
    public RecommendationDTO createRecommendation(Long advisorId, Long fundId,
                                                   String action, String rationale) {
        User       advisor = resolveAdvisor(advisorId);
        MutualFund fund    = fundRepository.findById(fundId)
                .orElseThrow(() -> new ResourceNotFoundException("MutualFund", "id", fundId));

        if (!fund.isActive()) {
            throw new BadRequestException(
                    "Fund '" + fund.getFundName() + "' is inactive — cannot recommend it.");
        }

        String normalizedAction = validateAction(action);
        log.info("Advisor {} posted {} recommendation for fund {}", advisorId, normalizedAction, fundId);
        return toDTO(fund, advisor, normalizedAction,
                rationale != null && !rationale.isBlank() ? rationale : "Advisor recommendation.");
    }

    // ================================================================
    //  PRIVATE HELPERS
    // ================================================================

    /** Resolve and validate the advisor user. */
    private User resolveAdvisor(Long advisorId) {
        if (advisorId == null) {
            throw new BadRequestException("advisorId is required.");
        }
        return userRepository.findById(advisorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", advisorId));
    }

    /** LOW → HOLD, MODERATE → BUY, HIGH/VERY_HIGH → SELL (caution flag). */
    private String inferAction(MutualFund.RiskLevel level) {
        return switch (level) {
            case LOW      -> "HOLD";
            case MODERATE -> "BUY";
            case HIGH, VERY_HIGH -> "SELL";
        };
    }

    /** Validates and normalises action string. */
    private String validateAction(String action) {
        if (action == null || action.isBlank()) return "BUY";
        String upper = action.toUpperCase().trim();
        if (!upper.equals("BUY") && !upper.equals("HOLD") && !upper.equals("SELL")) {
            throw new BadRequestException("action must be BUY | HOLD | SELL, got: " + action);
        }
        return upper;
    }

    /** Entity → DTO mapper. */
    private RecommendationDTO toDTO(MutualFund fund, User advisor,
                                    String action, String rationale) {
        return RecommendationDTO.builder()
                .fundId(fund.getId())
                .fundName(fund.getFundName())
                .advisorId(advisor.getId())
                .advisorName(advisor.getName())
                .rationale(rationale)
                .action(action)
                .riskLevel(fund.getRiskLevel().name())
                .category(fund.getCategory())
                .returns(fund.getReturns().doubleValue())
                .date(LocalDate.now())
                .build();
    }
}
