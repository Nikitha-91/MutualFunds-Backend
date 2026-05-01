package com.klu.controller;

import com.klu.dto.ApiResponse;
import com.klu.dto.RecommendationDTO;
import com.klu.service.RecommendationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

@GetMapping("/top-buys")
    @PreAuthorize("hasAnyRole('ADVISOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<RecommendationDTO>>> getTopBuys(
            @RequestParam Long advisorId,
            @RequestParam(defaultValue = "10") int limit) {

        if (limit < 1)  limit = 1;
        if (limit > 50) limit = 50;

        List<RecommendationDTO> recs =
                recommendationService.getTopBuyOpportunities(advisorId, limit);
        return ResponseEntity.ok(
                ApiResponse.ok("Top BUY opportunities", recs));
    }

@GetMapping("/by-risk")
    @PreAuthorize("hasAnyRole('ADVISOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<RecommendationDTO>>> getByRisk(
            @RequestParam Long   advisorId,
            @RequestParam String riskLevel) {

        List<RecommendationDTO> recs =
                recommendationService.getRecommendationsByRisk(advisorId, riskLevel);
        return ResponseEntity.ok(
                ApiResponse.ok("Recommendations for risk level: " + riskLevel.toUpperCase(), recs));
    }

@GetMapping("/by-category")
    @PreAuthorize("hasAnyRole('ADVISOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<RecommendationDTO>>> getByCategory(
            @RequestParam Long   advisorId,
            @RequestParam String category) {

        List<RecommendationDTO> recs =
                recommendationService.getRecommendationsByCategory(advisorId, category);
        return ResponseEntity.ok(
                ApiResponse.ok("Recommendations for category: " + category.toUpperCase(), recs));
    }

@PostMapping("/custom")
    @PreAuthorize("hasAnyRole('ADVISOR','ADMIN')")
    public ResponseEntity<ApiResponse<RecommendationDTO>> createCustomRecommendation(
            @RequestBody CustomRecommendationRequest req) {

        RecommendationDTO rec = recommendationService.createRecommendation(
                req.advisorId(), req.fundId(), req.action(), req.rationale());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Recommendation created", rec));
    }

public record CustomRecommendationRequest(
            Long   advisorId,
            Long   fundId,
            String action,
            String rationale
    ) {}
}
