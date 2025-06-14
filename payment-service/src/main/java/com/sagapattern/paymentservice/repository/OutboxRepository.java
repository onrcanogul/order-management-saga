package com.sagapattern.paymentservice.repository;

import com.sagapattern.paymentservice.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<Outbox, UUID> {
    List<Outbox> findByPublishedFalse();
}
