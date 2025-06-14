package com.sagapattern.orderservice.service.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapattern.common.event.OrderCreatedEvent;
import com.sagapattern.orderservice.entity.Outbox;
import com.sagapattern.orderservice.repository.OutboxRepository;
import com.sagapattern.orderservice.service.order.EventPublisher;
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

    public void publishOrderCreatedEvent() {
        log.info("Publishing order created event job was triggered");
        List<Outbox> outboxes = outboxRepository.findByPublishedFalse();
        outboxes.forEach(outbox -> {
            try {
                eventPublisher.sendCreateOrderEvent(objectMapper.readValue(outbox.getPayload(), OrderCreatedEvent.class));
                log.info("Event has been published");
                outbox.setPublished(true);
                outboxRepository.save(outbox);
                log.info("Outbox has been published");
            }
            catch (Exception e) {
                log.error("Error while publishing outbox events: {}", e.getMessage());
            }
        });
    }

}
