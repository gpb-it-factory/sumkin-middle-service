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
@EqualsAndHashCode
@ToString
public class AccountGpb {
    @Id
    private UUID id;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "tg_id")
    private Long tgId;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "account_name")
    private String accountName;
}