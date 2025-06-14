package com.sagapattern.orderservice.service.outbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class OutboxScheduler {
    private final OutboxPublisher outboxPublisher;

    public OutboxScheduler(OutboxPublisher outboxPublisher) {
        this.outboxPublisher = outboxPublisher;
    }

    @Scheduled(fixedRate = 5000)
    public void publishOrderCreatedEvent() {
        outboxPublisher.publishOrderCreatedEvent(); // seperated -> we can write some unit tests
    }
}
