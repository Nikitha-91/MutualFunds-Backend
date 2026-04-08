package com.klu.service;

import com.klu.dto.MutualFundRequestDTO;
import com.klu.dto.MutualFundResponseDTO;
import com.klu.entity.MutualFund;
import com.klu.exception.BadRequestException;
import com.klu.exception.DuplicateResourceException;
import com.klu.exception.ResourceNotFoundException;
import com.klu.repository.MutualFundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service layer for Mutual Fund CRUD operations.
 *
 * All write operations are restricted to ADMIN role
 * (enforced at controller level via {@code @PreAuthorize}).
 *
 * Soft-delete is used: deleting a fund sets {@code active = false}
 * rather than removing the row, preserving investment history.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MutualFundService {

    private final MutualFundRepository fundRepository;

    // ================================================================
    //  CREATE
    // ================================================================

    /**
     * Add a new mutual fund to the platform (Admin only).
     *
     * @param req request payload with fund details
     * @return saved fund as a response DTO
     * @throws BadRequestException        if required fields are missing
     * @throws DuplicateResourceException if a fund with the same name already exists
     */
    @Transactional
    public MutualFundResponseDTO addFund(MutualFundRequestDTO req) {
        validateRequest(req);

        if (fundRepository.existsByFundNameIgnoreCase(req.getFundName())) {
            throw new DuplicateResourceException("MutualFund", "fundName", req.getFundName());
        }

        MutualFund fund = MutualFund.builder()
                .fundName(req.getFundName().trim())
                .category(req.getCategory().toUpperCase().trim())
                .riskLevel(resolveRiskLevel(req.getRiskLevel()))
                .returns(req.getReturns())
                .nav(req.getNav())
                .description(req.getDescription())
                .active(true)
                .build();

        fund = fundRepository.save(fund);
        log.info("New mutual fund added: id={}, name={}", fund.getId(), fund.getFundName());
        return toDTO(fund);
    }

    // ================================================================
    //  READ
    // ================================================================

    /**
     * Get all active (non-deleted) funds.
     */
    @Transactional(readOnly = true)
    public List<MutualFundResponseDTO> getAllFunds() {
        return fundRepository.findByActiveTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Get a single fund by its ID.
     *
     * @throws ResourceNotFoundException if not found
     */
    @Transactional(readOnly = true)
    public MutualFundResponseDTO getFundById(Long id) {
        return toDTO(findOrThrow(id));
    }

    /**
     * Search active funds by partial name match (case-insensitive).
     */
    @Transactional(readOnly = true)
    public List<MutualFundResponseDTO> searchByName(String fundName) {
        return fundRepository.findByFundNameContainingIgnoreCase(fundName)
                .stream()
                .filter(MutualFund::isActive)
                .map(this::toDTO)
                .toList();
    }

    /**
     * Filter active funds by category (e.g. EQUITY, DEBT).
     */
    @Transactional(readOnly = true)
    public List<MutualFundResponseDTO> getByCategory(String category) {
        return fundRepository.findByCategoryIgnoreCaseAndActiveTrue(category)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Filter active funds by risk level.
     */
    @Transactional(readOnly = true)
    public List<MutualFundResponseDTO> getByRiskLevel(String riskLevel) {
        return fundRepository.findByRiskLevelAndActiveTrue(resolveRiskLevel(riskLevel))
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Get all active funds with returns ≥ the given minimum.
     */
    @Transactional(readOnly = true)
    public List<MutualFundResponseDTO> getByMinReturns(BigDecimal minReturns) {
        return fundRepository.findByMinReturns(minReturns)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ================================================================
    //  UPDATE
    // ================================================================

    /**
     * Fully update an existing fund (Admin only).
     * All fields are replaced — partial update is handled at controller level
     * by passing the same value for unchanged fields.
     *
     * @param id  fund primary key
     * @param req new values
     * @return updated fund as DTO
     * @throws ResourceNotFoundException if fund not found
     */
    @Transactional
    public MutualFundResponseDTO updateFund(Long id, MutualFundRequestDTO req) {
        validateRequest(req);

        MutualFund fund = findOrThrow(id);
        fund.setFundName(req.getFundName().trim());
        fund.setCategory(req.getCategory().toUpperCase().trim());
        fund.setRiskLevel(resolveRiskLevel(req.getRiskLevel()));
        fund.setReturns(req.getReturns());
        fund.setNav(req.getNav());
        fund.setDescription(req.getDescription());

        fund = fundRepository.save(fund);
        log.info("Mutual fund updated: id={}", fund.getId());
        return toDTO(fund);
    }

    // ================================================================
    //  DELETE (soft)
    // ================================================================

    /**
     * Soft-delete a fund — sets {@code active = false}.
     * The fund row is retained to preserve investment history.
     *
     * @param id fund primary key
     * @throws ResourceNotFoundException if fund not found
     */
    @Transactional
    public void deleteFund(Long id) {
        MutualFund fund = findOrThrow(id);
        fund.setActive(false);
        fundRepository.save(fund);
        log.info("Mutual fund soft-deleted: id={}", id);
    }

    // ================================================================
    //  PRIVATE HELPERS
    // ================================================================

    private MutualFund findOrThrow(Long id) {
        return fundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MutualFund", "id", id));
    }

    private MutualFund.RiskLevel resolveRiskLevel(String riskLevel) {
        try {
            return MutualFund.RiskLevel.valueOf(riskLevel.toUpperCase().trim());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BadRequestException(
                    "Invalid riskLevel '" + riskLevel + "'. Accepted: LOW | MODERATE | HIGH | VERY_HIGH");
        }
    }

    private void validateRequest(MutualFundRequestDTO req) {
        if (req.getFundName() == null || req.getFundName().isBlank())
            throw new BadRequestException("fundName is required.");
        if (req.getCategory() == null || req.getCategory().isBlank())
            throw new BadRequestException("category is required.");
        if (req.getRiskLevel() == null || req.getRiskLevel().isBlank())
            throw new BadRequestException("riskLevel is required.");
        if (req.getReturns() == null)
            throw new BadRequestException("returns is required.");
        if (req.getNav() == null)
            throw new BadRequestException("nav is required.");
    }

    /** Entity → Response DTO */
    private MutualFundResponseDTO toDTO(MutualFund fund) {
        return MutualFundResponseDTO.builder()
                .id(fund.getId())
                .fundName(fund.getFundName())
                .category(fund.getCategory())
                .riskLevel(fund.getRiskLevel().name())
                .returns(fund.getReturns())
                .nav(fund.getNav())
                .description(fund.getDescription())
                .active(fund.isActive())
                .build();
    }
}
