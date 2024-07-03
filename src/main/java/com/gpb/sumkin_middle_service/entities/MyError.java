package com.gpb.sumkin_middle_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "my_error",schema = "gpb")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyError {
    private String message;
    private String type;
    private int code;
    @Id
    @Column(nullable = false, name = "trace_id")
    private UUID traceId;
}
