package com.klu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "investments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund_id", nullable = false)
    private MutualFund mutualFund;

@Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

@Column(name = "investment_date", nullable = false)
    private LocalDate date;

@Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal units;

@Column(name = "purchase_nav", nullable = false, precision = 14, scale = 4)
    private BigDecimal purchaseNav;

@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvestmentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private InvestmentStatus status = InvestmentStatus.ACTIVE;

public enum InvestmentType {
        LUMP_SUM, SIP
    }

    public enum InvestmentStatus {
        ACTIVE, REDEEMED, CANCELLED
    }
}
