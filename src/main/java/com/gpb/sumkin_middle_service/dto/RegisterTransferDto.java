package com.gpb.sumkin_middle_service.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class RegisterTransferDto {

    private final String from;
    private final String to;
    @Positive(message = "Сумма должна быть положительной")
    private final BigDecimal amount;
}
