package com.gpb.sumkin_middle_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "user_gpb", schema = "gpb")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserGpb {
    @Id
    @Column(nullable = false)
    private UUID id;
    @Column(name = "tg_id", nullable = false, unique = true)
    private long tgId;
    private String tgUsername;
}
