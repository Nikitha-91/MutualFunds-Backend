package com.klu.service;

import com.klu.dto.RecommendationDTO;
import com.klu.entity.MutualFund;
import com.klu.entity.User;
import com.klu.exception.BadRequestException;
import com.klu.exception.ResourceNotFoundException;
import com.klu.repository.MutualFundRepository;
import com.klu.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);

    private final MutualFundRepository fundRepository;
    private final UserRepository       userRepository;

    public RecommendationService(MutualFundRepository fundRepository,
                                 UserRepository userRepository) {
        this.fundRepository = fundRepository;
        this.userRepository = userRepository;
    }

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

@Transactional(readOnly = true)
    public List<RecommendationDTO> getRecommendationsByCategory(Long advisorId, String category) {
        User advisor = resolveAdvisor(advisorId);

        return fundRepository.findByCategoryIgnoreCaseAndActiveTrue(category).stream()
                .sorted(Comparator.comparingDouble(f -> -f.getReturns().doubleValue()))
                .map(f -> toDTO(f, advisor, "BUY",
                        "Strong " + category.toUpperCase() + " category fund."))
                .collect(Collectors.toList());
    }

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

private User resolveAdvisor(Long advisorId) {
        if (advisorId == null) {
            throw new BadRequestException("advisorId is required.");
        }
        return userRepository.findById(advisorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", advisorId));
    }

private String inferAction(MutualFund.RiskLevel level) {
        return switch (level) {
            case LOW      -> "HOLD";
            case MODERATE -> "BUY";
            case HIGH, VERY_HIGH -> "SELL";
        };
    }

private String validateAction(String action) {
        if (action == null || action.isBlank()) return "BUY";
        String upper = action.toUpperCase().trim();
        if (!upper.equals("BUY") && !upper.equals("HOLD") && !upper.equals("SELL")) {
            throw new BadRequestException("action must be BUY | HOLD | SELL, got: " + action);
        }
        return upper;
    }

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
