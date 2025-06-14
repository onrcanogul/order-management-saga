package com.sagapattern.paymentservice.service.outbox;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OutboxScheduler {
    private final OutboxPublisher outboxPublisher;

    public OutboxScheduler(OutboxPublisher outboxPublisher) {
        this.outboxPublisher = outboxPublisher;
    }

    @Scheduled(fixedRate = 5000)
    public void publishPaymentSucceededEvent() {
        outboxPublisher.publishUnsentEvents();
    }

}
