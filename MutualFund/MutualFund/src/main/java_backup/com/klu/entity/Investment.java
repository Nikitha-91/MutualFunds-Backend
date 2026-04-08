package com.klu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Records a single investment made by a {@link User} into a {@link MutualFund}.
 *
 * ┌─────────────────┬───────────────────────────────────────────────────────┐
 * │  Field          │  Description                                          │
 * ├─────────────────┼───────────────────────────────────────────────────────┤
 * │  id             │  Auto-generated primary key                           │
 * │  userId         │  FK → users.id  (exposed directly in API responses)   │
 * │  fundId         │  FK → mutual_funds.id  (exposed in API responses)     │
 * │  amount         │  Invested amount in INR                               │
 * │  date           │  Calendar date of investment  (maps to investment_date)│
 * │  units          │  Units = amount ÷ NAV  (computed at buy time)         │
 * │  purchaseNav    │  NAV at time of purchase (for gain/loss calc)         │
 * │  type           │  LUMP_SUM | SIP                                       │
 * │  status         │  ACTIVE | REDEEMED | CANCELLED                        │
 * └─────────────────┴───────────────────────────────────────────────────────┘
 */
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

    // ── Foreign key relationships (JPA) ────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund_id", nullable = false)
    private MutualFund mutualFund;

    // ── Core investment fields ──────────────────────────────────────

    /** Amount invested in INR (e.g. 10000.00) */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /**
     * Date of investment.
     * Column name is {@code investment_date} in the DB;
     * exposed as {@code date} in the API layer.
     */
    @Column(name = "investment_date", nullable = false)
    private LocalDate date;

    // ── Derived / computed fields ───────────────────────────────────

    /** Units purchased = amount ÷ purchaseNav (computed at buy time) */
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal units;

    /** NAV per unit at the time of investment */
    @Column(name = "purchase_nav", nullable = false, precision = 14, scale = 4)
    private BigDecimal purchaseNav;

    // ── Classification ──────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvestmentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private InvestmentStatus status = InvestmentStatus.ACTIVE;

    // ── Enums ───────────────────────────────────────────────────────

    public enum InvestmentType {
        LUMP_SUM, SIP
    }

    public enum InvestmentStatus {
        ACTIVE, REDEEMED, CANCELLED
    }
}
