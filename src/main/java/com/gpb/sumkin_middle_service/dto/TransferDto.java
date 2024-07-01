package com.gpb.sumkin_middle_service.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class TransferDto {

    private final String from;
    private final String to;
    private final BigDecimal amount;
}
