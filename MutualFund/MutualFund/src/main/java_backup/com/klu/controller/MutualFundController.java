package com.klu.controller;

import com.klu.dto.ApiResponse;
import com.klu.dto.MutualFundRequestDTO;
import com.klu.dto.MutualFundResponseDTO;
import com.klu.service.MutualFundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for Mutual Fund management.
 *
 * Base URL : /api/funds
 *
 * ┌────────────────────────────────────────────────────────────────────────┐
 * │  Method  │  URL                              │  Access                │
 * ├──────────┼───────────────────────────────────┼────────────────────────┤
 * │  POST    │  /api/funds                       │  ADMIN only            │
 * │  GET     │  /api/funds                       │  Any authenticated     │
 * │  GET     │  /api/funds/{id}                  │  Any authenticated     │
 * │  GET     │  /api/funds/search?name=...       │  Any authenticated     │
 * │  GET     │  /api/funds/category/{cat}        │  Any authenticated     │
 * │  GET     │  /api/funds/risk/{level}          │  Any authenticated     │
 * │  GET     │  /api/funds/filter?minReturns=... │  Any authenticated     │
 * │  PUT     │  /api/funds/{id}                  │  ADMIN only            │
 * │  DELETE  │  /api/funds/{id}                  │  ADMIN only            │
 * └──────────┴───────────────────────────────────┴────────────────────────┘
 */
@RestController
@RequestMapping("/api/funds")
@RequiredArgsConstructor
public class MutualFundController {

    private final MutualFundService fundService;

    // ================================================================
    //  CREATE — Admin only
    // ================================================================

    /**
     * Add a new mutual fund.
     *
     * <p>Request body:
     * <pre>
     * {
     *   "fundName":    "Axis Bluechip Fund",
     *   "category":   "EQUITY",
     *   "riskLevel":  "HIGH",
     *   "returns":    14.50,
     *   "nav":        45.23,
     *   "description": "Large-cap equity fund..."
     * }
     * </pre>
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MutualFundResponseDTO>> addFund(
            @RequestBody MutualFundRequestDTO request) {
        MutualFundResponseDTO fund = fundService.addFund(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Fund added successfully", fund));
    }

    // ================================================================
    //  READ — Any authenticated user
    // ================================================================

    /**
     * Get all active mutual funds.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MutualFundResponseDTO>>> getAllFunds() {
        return ResponseEntity.ok(
                ApiResponse.ok("All active funds", fundService.getAllFunds()));
    }

    /**
     * Get a single fund by its database ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MutualFundResponseDTO>> getFundById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(fundService.getFundById(id)));
    }

    /**
     * Search funds by partial name (case-insensitive).
     *
     * @param name partial fund name, e.g. GET /api/funds/search?name=axis
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MutualFundResponseDTO>>> searchByName(
            @RequestParam String name) {
        return ResponseEntity.ok(
                ApiResponse.ok("Search results", fundService.searchByName(name)));
    }

    /**
     * Filter funds by category (EQUITY, DEBT, HYBRID, LIQUID, ELSS, INDEX).
     *
     * @param category e.g. GET /api/funds/category/EQUITY
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<MutualFundResponseDTO>>> getByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(
                ApiResponse.ok("Funds in category: " + category,
                        fundService.getByCategory(category)));
    }

    /**
     * Filter funds by risk level (LOW, MODERATE, HIGH, VERY_HIGH).
     *
     * @param level e.g. GET /api/funds/risk/HIGH
     */
    @GetMapping("/risk/{level}")
    public ResponseEntity<ApiResponse<List<MutualFundResponseDTO>>> getByRiskLevel(
            @PathVariable String level) {
        return ResponseEntity.ok(
                ApiResponse.ok("Funds with risk level: " + level,
                        fundService.getByRiskLevel(level)));
    }

    /**
     * Filter funds by minimum annualised returns.
     *
     * @param minReturns e.g. GET /api/funds/filter?minReturns=10.0
     */
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<MutualFundResponseDTO>>> getByMinReturns(
            @RequestParam BigDecimal minReturns) {
        return ResponseEntity.ok(
                ApiResponse.ok("Funds with returns ≥ " + minReturns + "%",
                        fundService.getByMinReturns(minReturns)));
    }

    // ================================================================
    //  UPDATE — Admin only
    // ================================================================

    /**
     * Fully update an existing fund's details.
     *
     * @param id      fund primary key
     * @param request new values (all fields replaced)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MutualFundResponseDTO>> updateFund(
            @PathVariable Long id,
            @RequestBody MutualFundRequestDTO request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Fund updated successfully", fundService.updateFund(id, request)));
    }

    // ================================================================
    //  DELETE — Admin only (soft delete)
    // ================================================================

    /**
     * Soft-delete a fund (sets active = false).
     * The fund record is retained to preserve investment history.
     *
     * @param id fund primary key
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteFund(@PathVariable Long id) {
        fundService.deleteFund(id);
        return ResponseEntity.ok(ApiResponse.ok("Fund deleted (deactivated) successfully", null));
    }
}
