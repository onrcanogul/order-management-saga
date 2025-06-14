package com.sagapattern.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Inbox {
    @Id
    private UUID idempotentToken;
    private String payload;
    private String type;
    private boolean processed = false;
    private LocalDateTime receivedAt;
}
