package com.gpb.sumkin_middle_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {

    private String from;
    private String to;
    private BigDecimal amount;
}
