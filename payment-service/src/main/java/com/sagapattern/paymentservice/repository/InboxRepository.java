package com.sagapattern.paymentservice.repository;

import com.sagapattern.paymentservice.entity.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InboxRepository extends JpaRepository<Inbox, UUID> {
    List<Inbox> findByProcessedFalse();
}
