package com.gpb.sumkin_middle_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "account_gpb",schema = "gpb")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountGpb {
    @Id
    private UUID id;
    @Column(name = "user_id")
    private UUID userId;
    private BigDecimal amount;
    private String accountName;
}