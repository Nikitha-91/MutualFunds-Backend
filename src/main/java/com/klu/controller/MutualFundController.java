package com.klu.controller;

import com.klu.dto.ApiResponse;
import com.klu.dto.MutualFundRequestDTO;
import com.klu.dto.MutualFundResponseDTO;
import com.klu.service.MutualFundService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/funds")
public class MutualFundController {

    private final MutualFundService fundService;

    public MutualFundController(MutualFundService fundService) {
        this.fundService = fundService;
    }

@PostMapping
    @PreAuthorize("hasAnyRole('INVESTOR','ADMIN')")
    public ResponseEntity<ApiResponse<MutualFundResponseDTO>> addFund(
            @RequestBody MutualFundRequestDTO request) {
        MutualFundResponseDTO fund = fundService.addFund(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Fund added successfully", fund));
    }

@GetMapping
    public ResponseEntity<ApiResponse<List<MutualFundResponseDTO>>> getAllFunds() {
        return ResponseEntity.ok(
                ApiResponse.ok("All active funds", fundService.getAllFunds()));
    }

@GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MutualFundResponseDTO>> getFundById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(fundService.getFundById(id)));
    }

@GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MutualFundResponseDTO>>> searchByName(
            @RequestParam String name) {
        return ResponseEntity.ok(
                ApiResponse.ok("Search results", fundService.searchByName(name)));
    }

@GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<MutualFundResponseDTO>>> getByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(
                ApiResponse.ok("Funds in category: " + category,
                        fundService.getByCategory(category)));
    }

@GetMapping("/risk/{level}")
    public ResponseEntity<ApiResponse<List<MutualFundResponseDTO>>> getByRiskLevel(
            @PathVariable String level) {
        return ResponseEntity.ok(
                ApiResponse.ok("Funds with risk level: " + level,
                        fundService.getByRiskLevel(level)));
    }

@GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<MutualFundResponseDTO>>> getByMinReturns(
            @RequestParam BigDecimal minReturns) {
        return ResponseEntity.ok(
                ApiResponse.ok("Funds with returns ≥ " + minReturns + "%",
                        fundService.getByMinReturns(minReturns)));
    }

@PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MutualFundResponseDTO>> updateFund(
            @PathVariable Long id,
            @RequestBody MutualFundRequestDTO request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Fund updated successfully", fundService.updateFund(id, request)));
    }

@DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteFund(@PathVariable Long id) {
        fundService.deleteFund(id);
        return ResponseEntity.ok(ApiResponse.ok("Fund deleted (deactivated) successfully", null));
    }
}
