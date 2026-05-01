package com.klu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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

@Column(name = "fund_name", nullable = false, length = 150)
    private String fundName;

@Column(nullable = false, length = 50)
    private String category;

@Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    private RiskLevel riskLevel;

@Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal returns;

@Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal nav;

@Column(columnDefinition = "TEXT")
    private String description;

@Column(nullable = false)
    @Builder.Default
    private boolean active = true;

@OneToMany(mappedBy = "mutualFund", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Investment> investments;

public enum RiskLevel {
        LOW,
        MODERATE,
        HIGH,
        VERY_HIGH
    }
}
