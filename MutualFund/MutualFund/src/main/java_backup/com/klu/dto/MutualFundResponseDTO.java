package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Outbound DTO representing a Mutual Fund returned to the client.
 * Does NOT expose the {@code investments} list or internal flags directly.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MutualFundResponseDTO {

    private Long id;
    private String fundName;
    private String category;
    private String riskLevel;
    private BigDecimal returns;
    private BigDecimal nav;
    private String description;
    private boolean active;
}
