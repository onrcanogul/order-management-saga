package com.sagapattern.orderservice.service.inbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapattern.common.command.PaymentFailedCommand;
import com.sagapattern.common.command.PaymentSucceededCommand;
import com.sagapattern.common.enums.OrderStatus;
import com.sagapattern.orderservice.entity.Inbox;
import com.sagapattern.orderservice.repository.InboxRepository;
import com.sagapattern.orderservice.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class InboxProcessor {
    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;
    private final OrderService service;

    public InboxProcessor(InboxRepository inboxRepository, ObjectMapper objectMapper, OrderService service) {
        this.inboxRepository = inboxRepository;
        this.objectMapper = objectMapper;
        this.service = service;
    }

    public void processInboxes() {
        List<Inbox> inboxes = inboxRepository.findByProcessedFalse();
        inboxes.forEach(this::processInboxSafely);
    }

    @Transactional
    public void processInboxSafely(Inbox inbox) {
        try {
            if (Objects.equals(inbox.getType(), "PaymentSucceededCommand")) {
                PaymentSucceededCommand command = objectMapper.readValue(inbox.getPayload(), PaymentSucceededCommand.class);
                service.updateStatus(OrderStatus.DONE, command.getOrderId());
            } else if (Objects.equals(inbox.getType(), "PaymentFailedCommand")) {
                PaymentFailedCommand command = objectMapper.readValue(inbox.getPayload(), PaymentFailedCommand.class);
                service.updateStatus(OrderStatus.REJECTED, command.getOrderId());
            } else {
                throw new IllegalArgumentException("Unknown event type: " + inbox.getType());
            }
            inbox.setProcessed(true);
            inboxRepository.save(inbox);
        } catch (Exception e) {
            log.error("Failed to process inbox with ID {}: {}", inbox.getIdempotentToken(), e.getMessage());
        }
    }
}
