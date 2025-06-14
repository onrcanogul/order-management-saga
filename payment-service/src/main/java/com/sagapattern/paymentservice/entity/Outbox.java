package com.sagapattern.paymentservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Outbox {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idempotentToken;
    private String aggregateType;
    private String aggregateId;

    private String type;
    @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private String payload;

    private boolean published = false;
    private Instant createdAt = Instant.now();
}
