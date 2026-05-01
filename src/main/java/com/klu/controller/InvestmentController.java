package com.klu.controller;

import com.klu.dto.ApiResponse;
import com.klu.dto.InvestmentRequestDTO;
import com.klu.dto.InvestmentResponseDTO;
import com.klu.dto.TotalInvestmentDTO;
import com.klu.service.InvestmentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

@PostMapping
    @PreAuthorize("hasAnyRole('INVESTOR','ADMIN')")
    public ResponseEntity<ApiResponse<InvestmentResponseDTO>> invest(
            @RequestBody InvestmentRequestDTO request) {

        InvestmentResponseDTO result = investmentService.invest(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Investment placed successfully", result));
    }

@GetMapping
    @PreAuthorize("hasAnyRole('INVESTOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<InvestmentResponseDTO>>> getAllInvestments() {

        List<InvestmentResponseDTO> list = investmentService.getAllInvestments();

        return ResponseEntity.ok(
                ApiResponse.ok("All investments fetched successfully", list));
    }

@GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('INVESTOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<InvestmentResponseDTO>>> getUserInvestments(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                ApiResponse.ok("User investments fetched",
                        investmentService.getUserInvestments(userId)));
    }

    @GetMapping("/user/{userId}/active")
    @PreAuthorize("hasAnyRole('INVESTOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<InvestmentResponseDTO>>> getActiveUserInvestments(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                ApiResponse.ok("Active investments fetched",
                        investmentService.getActiveUserInvestments(userId)));
    }

    @GetMapping("/user/{userId}/fund/{fundId}")
    @PreAuthorize("hasAnyRole('INVESTOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<InvestmentResponseDTO>>> getUserInvestmentsByFund(
            @PathVariable Long userId,
            @PathVariable Long fundId) {

        return ResponseEntity.ok(
                ApiResponse.ok("Fund-wise investments fetched",
                        investmentService.getUserInvestmentsByFund(userId, fundId)));
    }

@GetMapping("/user/{userId}/total")
    @PreAuthorize("hasAnyRole('INVESTOR','ADMIN')")
    public ResponseEntity<ApiResponse<TotalInvestmentDTO>> calculateTotal(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                ApiResponse.ok("Total investment calculated",
                        investmentService.calculateTotalInvestment(userId)));
    }

@PutMapping("/{id}/redeem")
    @PreAuthorize("hasAnyRole('INVESTOR','ADMIN')")
    public ResponseEntity<ApiResponse<InvestmentResponseDTO>> redeem(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.ok("Investment redeemed successfully",
                        investmentService.redeem(id)));
    }
}