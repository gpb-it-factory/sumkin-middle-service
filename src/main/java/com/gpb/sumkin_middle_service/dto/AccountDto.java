package com.gpb.sumkin_middle_service.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@Builder
@Entity
public class AccountDto {

    @Id
    private UUID id;
    private String accountName;
    private BigDecimal amount;
}
