package com.klu.util;

import com.klu.dto.*;
import com.klu.entity.Investment;
import com.klu.entity.MutualFund;
import com.klu.entity.User;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Stateless mapper utility that converts JPA entities → DTOs.
 *
 * <p>Keeps mapping logic out of services and controllers,
 * following the Single Responsibility principle.
 */
public final class MappingUtil {

    private MappingUtil() { /* utility class */ }

    // ---- User ----

    public static UserResponseDTO toUserResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .enabled(user.isEnabled())
                .build();
    }

    // ---- MutualFund ----

    public static MutualFundResponseDTO toFundResponse(MutualFund fund) {
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

    // ---- Investment ----

    public static InvestmentResponseDTO toInvestmentResponse(Investment inv) {
        BigDecimal currentNav     = inv.getMutualFund().getNav();
        BigDecimal currentValue   = inv.getUnits().multiply(currentNav).setScale(2, RoundingMode.HALF_UP);
        BigDecimal gainLoss       = currentValue.subtract(inv.getAmount()).setScale(2, RoundingMode.HALF_UP);

        return InvestmentResponseDTO.builder()
                .id(inv.getId())
                .userId(inv.getUser().getId())
                .userName(inv.getUser().getName())
                .fundId(inv.getMutualFund().getId())
                .fundName(inv.getMutualFund().getFundName())
                .amount(inv.getAmount())
                .units(inv.getUnits())
                .purchaseNav(inv.getPurchaseNav())
                .currentValue(currentValue)
                .gainLoss(gainLoss)
                .date(inv.getDate())
                .type(inv.getType().name())
                .status(inv.getStatus().name())
                .build();
    }
}
