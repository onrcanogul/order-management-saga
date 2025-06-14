package com.sagapattern.paymentservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Inbox {
    @Id
    private UUID idempotentToken;
    private String payload;
    private String type;
    private boolean processed = false;
    private LocalDateTime receivedAt;
}
