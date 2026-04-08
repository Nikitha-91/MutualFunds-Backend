package com.klu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a Mutual Fund available on the platform.
 *
 * ┌──────────────┬──────────────────────────────────────────────────┐
 * │   Field      │  Description                                     │
 * ├──────────────┼──────────────────────────────────────────────────┤
 * │ id           │  Auto-generated primary key                      │
 * │ fundName     │  Full name of the fund  (e.g. "Axis Bluechip")   │
 * │ category     │  EQUITY | DEBT | HYBRID | LIQUID                 │
 * │ riskLevel    │  LOW | MODERATE | HIGH | VERY_HIGH               │
 * │ returns      │  Annualised return % (e.g. 12.50)                │
 * │ nav          │  Net Asset Value per unit  (e.g. 45.23)          │
 * │ description  │  Free-text description of the fund               │
 * │ active       │  Soft-delete flag (false = not visible to users)  │
 * └──────────────┴──────────────────────────────────────────────────┘
 */
@Entity
@Table(name = "mutual_funds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MutualFund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the mutual fund */
    @Column(name = "fund_name", nullable = false, length = 150)
    private String fundName;

    /**
     * Broad classification of the fund.
     * Typical values: EQUITY, DEBT, HYBRID, LIQUID, ELSS, INDEX
     */
    @Column(nullable = false, length = 50)
    private String category;

    /**
     * Risk level of the fund.
     * LOW → safest, VERY_HIGH → most volatile.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    private RiskLevel riskLevel;

    /**
     * Annualised return percentage (e.g. 12.50 means 12.50 %).
     * Allows up to 6 integer digits and 2 decimal places.
     */
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal returns;

    /**
     * Net Asset Value — price of one unit of this fund in INR.
     * Allows up to 10 integer digits and 4 decimal places.
     */
    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal nav;

    /**
     * Optional free-text description about the fund's strategy,
     * objective, or any other relevant information.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Soft-delete flag; false = fund is deactivated / hidden from investors. */
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    /**
     * Investments placed in this fund.
     * Loaded lazily to avoid N+1 queries.
     */
    @OneToMany(mappedBy = "mutualFund", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Investment> investments;

    // ── Risk Level Enum ─────────────────────────────────────────────
    public enum RiskLevel {
        LOW,
        MODERATE,
        HIGH,
        VERY_HIGH
    }
}
