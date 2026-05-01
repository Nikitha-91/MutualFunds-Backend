package com.klu.service;

import com.klu.dto.InvestmentRequestDTO;
import com.klu.dto.InvestmentResponseDTO;
import com.klu.dto.TotalInvestmentDTO;
import com.klu.entity.Investment;
import com.klu.entity.MutualFund;
import com.klu.entity.User;
import com.klu.exception.BadRequestException;
import com.klu.exception.ResourceNotFoundException;
import com.klu.repository.InvestmentRepository;
import com.klu.repository.MutualFundRepository;
import com.klu.repository.UserRepository;
import com.klu.util.FinanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvestmentService {

    private static final Logger log = LoggerFactory.getLogger(InvestmentService.class);

    private final InvestmentRepository investmentRepository;
    private final UserRepository       userRepository;
    private final MutualFundRepository fundRepository;

    public InvestmentService(InvestmentRepository investmentRepository,
                             UserRepository userRepository,
                             MutualFundRepository fundRepository) {
        this.investmentRepository = investmentRepository;
        this.userRepository = userRepository;
        this.fundRepository = fundRepository;
    }@Transactional(readOnly = true)
    public List<InvestmentResponseDTO> getAllInvestments() {
        return investmentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

@Transactional
    public InvestmentResponseDTO invest(InvestmentRequestDTO req) {

if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Investment amount must be greater than zero.");
        }

User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", req.getUserId()));

        MutualFund fund = fundRepository.findById(req.getFundId())
                .orElseThrow(() -> new ResourceNotFoundException("MutualFund", "id", req.getFundId()));

        if (!fund.isActive()) {
            throw new BadRequestException("Fund '" + fund.getFundName() + "' is currently inactive.");
        }

BigDecimal nav   = fund.getNav();
        BigDecimal units = FinanceUtil.calculateUnits(req.getAmount(), nav);

Investment.InvestmentType type = resolveType(req.getType());

Investment investment = Investment.builder()
                .user(user)
                .mutualFund(fund)
                .amount(req.getAmount())
                .date(LocalDate.now())
                .units(units)
                .purchaseNav(nav)
                .type(type)
                .status(Investment.InvestmentStatus.ACTIVE)
                .build();

        investment = investmentRepository.save(investment);
        log.info("Investment created: id={}, userId={}, fundId={}, amount={}",
                investment.getId(), user.getId(), fund.getId(), req.getAmount());

        return toDTO(investment);
    }

@Transactional(readOnly = true)
    public List<InvestmentResponseDTO> getUserInvestments(Long userId) {
        validateUserExists(userId);
        return investmentRepository.findByUser_Id(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

@Transactional(readOnly = true)
    public List<InvestmentResponseDTO> getActiveUserInvestments(Long userId) {
        validateUserExists(userId);
        return investmentRepository
                .findByUser_IdAndStatus(userId, Investment.InvestmentStatus.ACTIVE)
                .stream()
                .map(this::toDTO)
                .toList();
    }

@Transactional(readOnly = true)
    public List<InvestmentResponseDTO> getUserInvestmentsByFund(Long userId, Long fundId) {
        return investmentRepository.findByUser_IdAndMutualFund_Id(userId, fundId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

@Transactional(readOnly = true)
    public TotalInvestmentDTO calculateTotalInvestment(Long userId) {
        validateUserExists(userId);

BigDecimal totalActive = investmentRepository.getTotalInvestedAmountByUserId(userId);
        BigDecimal grandTotal  = investmentRepository.getGrandTotalByUserId(userId);
        Long       fundsCount  = investmentRepository.getDistinctFundsCountByUserId(userId);
        Long       txCount     = investmentRepository.countByUser_Id(userId);

        totalActive = safeAmount(totalActive);
        grandTotal  = safeAmount(grandTotal);

List<Investment> active = investmentRepository
                .findByUser_IdAndStatus(userId, Investment.InvestmentStatus.ACTIVE);

        BigDecimal portfolioValue = active.stream()
                .map(i -> i.getUnits().multiply(i.getMutualFund().getNav()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

BigDecimal gainLoss        = portfolioValue.subtract(totalActive);
        BigDecimal gainLossPercent = FinanceUtil.gainLossPercent(totalActive, portfolioValue);

        return TotalInvestmentDTO.builder()
                .userId(userId)
                .totalActiveAmount(totalActive)
                .grandTotalAmount(grandTotal)
                .currentPortfolioValue(portfolioValue)
                .overallGainLoss(gainLoss)
                .overallGainLossPercent(gainLossPercent)
                .distinctFundsCount(fundsCount != null ? fundsCount : 0L)
                .totalTransactions(txCount != null ? txCount : 0L)
                .build();
    }

@Transactional
    public InvestmentResponseDTO redeem(Long investmentId) {
        Investment inv = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Investment", "id", investmentId));

        if (inv.getStatus() != Investment.InvestmentStatus.ACTIVE) {
            throw new BadRequestException("Only ACTIVE investments can be redeemed.");
        }

        inv.setStatus(Investment.InvestmentStatus.REDEEMED);
        inv = investmentRepository.save(inv);
        log.info("Investment redeemed: id={}", investmentId);
        return toDTO(inv);
    }

private Investment.InvestmentType resolveType(String type) {
        if (type == null || type.isBlank()) return Investment.InvestmentType.LUMP_SUM;
        try {
            return Investment.InvestmentType.valueOf(type.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid investment type '" + type + "'. Use LUMP_SUM or SIP.");
        }
    }

private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
    }

private BigDecimal safeAmount(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

private InvestmentResponseDTO toDTO(Investment inv) {
        BigDecimal currentNav   = inv.getMutualFund().getNav();
        BigDecimal currentValue = inv.getUnits()
                .multiply(currentNav)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal gainLoss     = currentValue.subtract(inv.getAmount())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal gainLossPct  = FinanceUtil.gainLossPercent(inv.getAmount(), currentValue);

        return InvestmentResponseDTO.builder()
                .id(inv.getId())
                .userId(inv.getUser().getId())
                .userName(inv.getUser().getName())
                .fundId(inv.getMutualFund().getId())
                .fundName(inv.getMutualFund().getFundName())
                .amount(inv.getAmount())
                .units(inv.getUnits())
                .purchaseNav(inv.getPurchaseNav())
                .date(inv.getDate())
                .type(inv.getType().name())
                .status(inv.getStatus().name())
                .currentValue(currentValue)
                .gainLoss(gainLoss)
                .gainLossPercent(gainLossPct)
                .build();
    }
}
