package com.gpb.sumkin_middle_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class AccountDto {

    private final Long accountId;
    private final String accountName;
    private final BigDecimal amount;
}
