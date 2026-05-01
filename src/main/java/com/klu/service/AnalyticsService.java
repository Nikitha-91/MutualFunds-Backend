package com.klu.service;

import com.klu.dto.MonthlyReportDTO;
import com.klu.dto.RiskReturnDTO;
import com.klu.dto.TopFundDTO;
import com.klu.entity.MutualFund;
import com.klu.exception.ResourceNotFoundException;
import com.klu.repository.InvestmentRepository;
import com.klu.repository.MonthlyInvestmentProjection;
import com.klu.repository.MutualFundRepository;
import com.klu.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    private final MutualFundRepository fundRepository;
    private final InvestmentRepository investmentRepository;
    private final UserRepository       userRepository;

    public AnalyticsService(MutualFundRepository fundRepository,
                            InvestmentRepository investmentRepository,
                            UserRepository userRepository) {
        this.fundRepository = fundRepository;
        this.investmentRepository = investmentRepository;
        this.userRepository = userRepository;
    }

@Transactional(readOnly = true)
    public List<TopFundDTO> getTopPerformingFunds(int limit) {
        log.debug("Analytics: fetching top {} performing funds", limit);

List<MutualFund> activeFunds = fundRepository.findByActiveTrue();

Map<Long, BigDecimal> aumMap = new HashMap<>();
        for (Object[] row : investmentRepository.getAumByFund()) {
            Long       fundId   = ((Number) row[0]).longValue();
            BigDecimal totalAum = (BigDecimal) row[1];
            aumMap.put(fundId, totalAum);
        }

Map<Long, Long> investorMap = new HashMap<>();
        for (Object[] row : investmentRepository.getInvestorCountByFund()) {
            Long fundId        = ((Number) row[0]).longValue();
            Long investorCount = ((Number) row[1]).longValue();
            investorMap.put(fundId, investorCount);
        }

activeFunds.sort(Comparator
                .comparing(MutualFund::getReturns, Comparator.reverseOrder())
                .thenComparing(f -> aumMap.getOrDefault(f.getId(), BigDecimal.ZERO),
                               Comparator.reverseOrder()));

List<TopFundDTO> result = new ArrayList<>();
        int rank = 1;
        for (MutualFund fund : activeFunds) {
            if (rank > limit) break;

            BigDecimal aum           = aumMap.getOrDefault(fund.getId(), BigDecimal.ZERO);
            Long       investorCount = investorMap.getOrDefault(fund.getId(), 0L);

            result.add(TopFundDTO.builder()
                    .id(fund.getId())
                    .fundName(fund.getFundName())
                    .category(fund.getCategory())
                    .riskLevel(fund.getRiskLevel().name())
                    .returns(fund.getReturns())
                    .nav(fund.getNav())
                    .navGrowth(fund.getReturns())   
                    .totalInvestors(investorCount)
                    .totalAum(aum.setScale(2, RoundingMode.HALF_UP))
                    .rank(rank)
                    .build());
            rank++;
        }

        log.debug("Analytics: returning {} top funds", result.size());
        return result;
    }

@Transactional(readOnly = true)
    public List<RiskReturnDTO> getRiskVsReturn() {
        log.debug("Analytics: building risk-vs-return matrix");

        List<MutualFund> activeFunds = fundRepository.findByActiveTrue();

Map<MutualFund.RiskLevel, List<MutualFund>> grouped = activeFunds.stream()
                .collect(Collectors.groupingBy(MutualFund::getRiskLevel));

List<RiskReturnDTO> result = new ArrayList<>();
        for (MutualFund.RiskLevel level : MutualFund.RiskLevel.values()) {
            List<MutualFund> bucket = grouped.getOrDefault(level, List.of());

            if (bucket.isEmpty()) {
                result.add(RiskReturnDTO.builder()
                        .riskLevel(level.name())
                        .fundCount(0)
                        .averageReturns(BigDecimal.ZERO)
                        .minReturns(BigDecimal.ZERO)
                        .maxReturns(BigDecimal.ZERO)
                        .medianReturns(BigDecimal.ZERO)
                        .funds(List.of())
                        .build());
                continue;
            }

            List<BigDecimal> returnsList = bucket.stream()
                    .map(MutualFund::getReturns)
                    .sorted()
                    .collect(Collectors.toList());

            BigDecimal avg    = average(returnsList);
            BigDecimal min    = returnsList.get(0);
            BigDecimal max    = returnsList.get(returnsList.size() - 1);
            BigDecimal median = median(returnsList);

List<RiskReturnDTO.FundSummary> fundSummaries = bucket.stream()
                    .sorted(Comparator.comparing(MutualFund::getReturns).reversed())
                    .map(f -> RiskReturnDTO.FundSummary.builder()
                            .id(f.getId())
                            .fundName(f.getFundName())
                            .returns(f.getReturns())
                            .nav(f.getNav())
                            .build())
                    .collect(Collectors.toList());

            result.add(RiskReturnDTO.builder()
                    .riskLevel(level.name())
                    .fundCount(bucket.size())
                    .averageReturns(avg)
                    .minReturns(min)
                    .maxReturns(max)
                    .medianReturns(median)
                    .funds(fundSummaries)
                    .build());
        }

        return result;
    }

@Transactional(readOnly = true)
    public MonthlyReportDTO getMonthlyReport(int year, Long userId) {
        log.debug("Analytics: monthly report year={} userId={}", year, userId);

if (userId != null && !userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

List<MonthlyInvestmentProjection> rows = (userId == null)
                ? investmentRepository.getMonthlyReport(year)
                : investmentRepository.getMonthlyReportByUser(userId, year);

Map<Integer, List<MonthlyInvestmentProjection>> byMonth = rows.stream()
                .collect(Collectors.groupingBy(MonthlyInvestmentProjection::getMonth));

List<MonthlyReportDTO.MonthEntry> monthEntries = new ArrayList<>();
        BigDecimal cumulativeTotal = BigDecimal.ZERO;
        BigDecimal periodTotal     = BigDecimal.ZERO;
        long       periodTxCount   = 0;
        Set<String> categoriesInPeriod = new LinkedHashSet<>();

        for (int m = 1; m <= 12; m++) {
            List<MonthlyInvestmentProjection> monthRows = byMonth.getOrDefault(m, List.of());
            if (monthRows.isEmpty()) continue;

            BigDecimal monthTotal = monthRows.stream()
                    .map(MonthlyInvestmentProjection::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            long monthTxCount = monthRows.stream()
                    .mapToLong(r -> r.getTransactionCount() != null ? r.getTransactionCount() : 0)
                    .sum();

            BigDecimal monthAvg = monthTxCount > 0
                    ? monthTotal.divide(BigDecimal.valueOf(monthTxCount), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            cumulativeTotal = cumulativeTotal.add(monthTotal);
            periodTotal     = periodTotal.add(monthTotal);
            periodTxCount  += monthTxCount;

List<MonthlyReportDTO.CategoryBreakdown> breakdown = monthRows.stream()
                    .filter(r -> r.getCategory() != null)
                    .map(r -> {
                        categoriesInPeriod.add(r.getCategory());
                        return MonthlyReportDTO.CategoryBreakdown.builder()
                                .category(r.getCategory())
                                .amount(r.getTotalAmount().setScale(2, RoundingMode.HALF_UP))
                                .transactions(r.getTransactionCount() != null ? r.getTransactionCount() : 0L)
                                .build();
                    })
                    .collect(Collectors.toList());

            monthEntries.add(MonthlyReportDTO.MonthEntry.builder()
                    .month(m)
                    .monthName(Month.of(m).getDisplayName(
                            java.time.format.TextStyle.FULL, Locale.ENGLISH))
                    .totalInvested(monthTotal)
                    .transactionCount(monthTxCount)
                    .averageAmount(monthAvg)
                    .cumulativeTotal(cumulativeTotal.setScale(2, RoundingMode.HALF_UP))
                    .categoryBreakdown(breakdown)
                    .build());
        }

        String scope = (userId == null) ? "PLATFORM" : "USER:" + userId;

        return MonthlyReportDTO.builder()
                .year(year)
                .reportScope(scope)
                .months(monthEntries)
                .totalInvestedInPeriod(periodTotal.setScale(2, RoundingMode.HALF_UP))
                .totalTransactionsInPeriod(periodTxCount)
                .activeFundsInPeriod(categoriesInPeriod.size())
                .build();
    }

private BigDecimal average(List<BigDecimal> values) {
        if (values.isEmpty()) return BigDecimal.ZERO;
        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal median(List<BigDecimal> sorted) {
        int size = sorted.size();
        if (size == 0) return BigDecimal.ZERO;
        if (size % 2 == 1) return sorted.get(size / 2);
        BigDecimal lo = sorted.get(size / 2 - 1);
        BigDecimal hi = sorted.get(size / 2);
        return lo.add(hi).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
    }
}
