package com.sagapattern.orderservice.repository;

import com.sagapattern.orderservice.entity.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InboxRepository extends JpaRepository<Inbox, UUID> {
    List<Inbox> findByProcessedFalse();
}
