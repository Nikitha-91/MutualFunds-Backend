package com.klu.controller;

import com.klu.dto.ApiResponse;
import com.klu.dto.MonthlyReportDTO;
import com.klu.dto.RiskReturnDTO;
import com.klu.dto.TopFundDTO;
import com.klu.service.AnalyticsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

@GetMapping("/top-funds")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','ADVISOR','INVESTOR')")
    public ResponseEntity<ApiResponse<List<TopFundDTO>>> getTopFunds(
            @RequestParam(defaultValue = "10") int limit) {

if (limit < 1)  limit = 1;
        if (limit > 50) limit = 50;

        List<TopFundDTO> funds = analyticsService.getTopPerformingFunds(limit);
        return ResponseEntity.ok(
                ApiResponse.ok("Top " + limit + " performing funds", funds));
    }

@GetMapping("/risk-vs-return")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','ADVISOR','INVESTOR')")
    public ResponseEntity<ApiResponse<List<RiskReturnDTO>>> getRiskVsReturn() {
        List<RiskReturnDTO> matrix = analyticsService.getRiskVsReturn();
        return ResponseEntity.ok(
                ApiResponse.ok("Risk vs Return comparison", matrix));
    }

@GetMapping("/monthly-report")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','ADVISOR','INVESTOR')")
    public ResponseEntity<ApiResponse<MonthlyReportDTO>> getMonthlyReport(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long    userId) {

        int reportYear = (year != null) ? year : LocalDate.now().getYear();

MonthlyReportDTO report = analyticsService.getMonthlyReport(reportYear, userId);
        return ResponseEntity.ok(
                ApiResponse.ok("Monthly investment report", report));
    }
}
