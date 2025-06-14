package com.sagapattern.paymentservice.service.inbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapattern.common.command.PaymentRequestCommand;
import com.sagapattern.common.event.OrderCreatedEvent;
import com.sagapattern.common.event.PaymentFailedEvent;
import com.sagapattern.common.event.PaymentSucceededEvent;
import com.sagapattern.paymentservice.entity.Inbox;
import com.sagapattern.paymentservice.entity.Outbox;
import com.sagapattern.paymentservice.repository.InboxRepository;
import com.sagapattern.paymentservice.repository.OutboxRepository;
import com.sagapattern.paymentservice.service.payment.PaymentService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class InboxProcessor {
    private final InboxRepository inboxRepository;
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    public InboxProcessor(
            InboxRepository inboxRepository,
            PaymentService paymentService,
            ObjectMapper objectMapper,
            OutboxRepository outboxRepository
    ) {
        this.inboxRepository = inboxRepository;
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public void processOrderCreatedEvent() {
        List<Inbox> inboxes =  inboxRepository.findByProcessedFalse();
        inboxes.forEach(this::processInboxSafely);
    }

    @Transactional
    public void processInboxSafely(Inbox inbox) {
        try {
            PaymentRequestCommand command = objectMapper.readValue(inbox.getPayload(), PaymentRequestCommand.class);
            boolean success = paymentService.handlePayment();
            if (success) {
                PaymentSucceededEvent paymentSucceededEvent = new PaymentSucceededEvent(UUID.randomUUID(), command.getOrderId());
                inbox.setProcessed(true);
                inboxRepository.save(inbox);
                outboxRepository.save(prepareOutbox(paymentSucceededEvent));
            }
            else {
                PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(UUID.randomUUID(), command.getOrderId());
                outboxRepository.save(prepareOutbox(paymentFailedEvent));
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to process inbox with ID {}: {}", inbox.getIdempotentToken(), e.getMessage());
        }
    }

    private Outbox prepareOutbox(Object event) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            Outbox outbox = new Outbox();
            outbox.setPayload(payload);
            outbox.setPublished(false);
            outbox.setType(event.getClass().getTypeName());
            outbox.setAggregateType(resolveAggregateType(event));
            return outbox;

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event to outbox", e);
        }
    }

    private String resolveAggregateType(Object event) {
        if (event instanceof PaymentSucceededEvent || event instanceof PaymentFailedEvent) {
            return "Payment";
        }
        if (event instanceof OrderCreatedEvent) {
            return "Order";
        }
        return "Unknown";
    }
}
