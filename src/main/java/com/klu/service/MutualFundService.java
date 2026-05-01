package com.klu.service;

import com.klu.dto.MutualFundRequestDTO;
import com.klu.dto.MutualFundResponseDTO;
import com.klu.entity.MutualFund;
import com.klu.exception.BadRequestException;
import com.klu.exception.DuplicateResourceException;
import com.klu.exception.ResourceNotFoundException;
import com.klu.repository.MutualFundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MutualFundService {

    private static final Logger log = LoggerFactory.getLogger(MutualFundService.class);

    private final MutualFundRepository fundRepository;

    public MutualFundService(MutualFundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }

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

@Transactional(readOnly = true)
    public List<MutualFundResponseDTO> getAllFunds() {
        return fundRepository.findByActiveTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

@Transactional(readOnly = true)
    public MutualFundResponseDTO getFundById(Long id) {
        return toDTO(findOrThrow(id));
    }

@Transactional(readOnly = true)
    public List<MutualFundResponseDTO> searchByName(String fundName) {
        return fundRepository.findByFundNameContainingIgnoreCase(fundName)
                .stream()
                .filter(MutualFund::isActive)
                .map(this::toDTO)
                .toList();
    }

@Transactional(readOnly = true)
    public List<MutualFundResponseDTO> getByCategory(String category) {
        return fundRepository.findByCategoryIgnoreCaseAndActiveTrue(category)
                .stream()
                .map(this::toDTO)
                .toList();
    }

@Transactional(readOnly = true)
    public List<MutualFundResponseDTO> getByRiskLevel(String riskLevel) {
        return fundRepository.findByRiskLevelAndActiveTrue(resolveRiskLevel(riskLevel))
                .stream()
                .map(this::toDTO)
                .toList();
    }

@Transactional(readOnly = true)
    public List<MutualFundResponseDTO> getByMinReturns(BigDecimal minReturns) {
        return fundRepository.findByMinReturns(minReturns)
                .stream()
                .map(this::toDTO)
                .toList();
    }

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

@Transactional
    public void deleteFund(Long id) {
        MutualFund fund = findOrThrow(id);
        fund.setActive(false);
        fundRepository.save(fund);
        log.info("Mutual fund soft-deleted: id={}", id);
    }

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
