package com.sagapattern.orderservice.service.inbox;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InboxScheduler {
    private final InboxProcessor inboxProcessor;

    public InboxScheduler(InboxProcessor inboxProcessor) {
        this.inboxProcessor = inboxProcessor;
    }

    @Scheduled(fixedRate = 5000)
    public void processInbox() {
        inboxProcessor.processInboxes();
    }
}
