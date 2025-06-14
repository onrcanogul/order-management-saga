package com.sagapattern.paymentservice.service.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapattern.common.command.PaymentRequestCommand;
import com.sagapattern.common.constant.RabbitMQConstants;
import com.sagapattern.paymentservice.entity.Inbox;
import com.sagapattern.paymentservice.repository.InboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class OrderEventConsumer {

    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;

    public OrderEventConsumer(InboxRepository inboxRepository, ObjectMapper objectMapper) {
        this.inboxRepository = inboxRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConstants.PAYMENT_REQUEST_QUEUE)
    public void receiveEvent(PaymentRequestCommand event) throws JsonProcessingException {

        if (inboxRepository.existsById(event.getIdempotentToken())) {
            log.info("Event already received. Skipping processing.");
            return;
        }

        inboxRepository.save(new Inbox(
                event.getIdempotentToken(),
                objectMapper.writeValueAsString(event),
                event.getClass().getTypeName(),
                false,
                LocalDateTime.now()
        ));
    }
}
