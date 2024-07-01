package com.gpb.sumkin_middle_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "gpb")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Id
    private UUID id;
    private UUID fromAccount;
    private UUID toAccount;
    private BigDecimal amount;
}
