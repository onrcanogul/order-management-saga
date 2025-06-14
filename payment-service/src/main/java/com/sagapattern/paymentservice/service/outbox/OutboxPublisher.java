package com.sagapattern.paymentservice.service.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapattern.common.event.PaymentFailedEvent;
import com.sagapattern.common.event.PaymentSucceededEvent;
import com.sagapattern.paymentservice.entity.Outbox;
import com.sagapattern.paymentservice.repository.OutboxRepository;
import com.sagapattern.paymentservice.service.payment.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OutboxPublisher {
    private final EventPublisher eventPublisher;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OutboxPublisher(EventPublisher eventPublisher, OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.eventPublisher = eventPublisher;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    public void publishUnsentEvents() {
        List<Outbox> events = outboxRepository.findByPublishedFalse();

        for (Outbox outbox : events) {
            try {
                Class<?> clazz = Class.forName(outbox.getType());
                Object event = objectMapper.readValue(outbox.getPayload(), clazz);

                if (event instanceof PaymentSucceededEvent succeededEvent) {
                    eventPublisher.sendPaymentSucceededEvent(succeededEvent);
                } else if (event instanceof PaymentFailedEvent failedEvent) {
                    eventPublisher.sendPaymentFailedEvent(failedEvent);
                } else {
                    log.warn("Unknown event type: {}", outbox.getType());
                    continue;
                }

                outbox.setPublished(true);
                outboxRepository.save(outbox);

            } catch (Exception e) {
                log.error("Error publishing outbox event: {}", e.getMessage(), e);
            }
        }
    }

}
