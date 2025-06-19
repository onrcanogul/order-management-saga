package com.sagapattern.orderservice.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapattern.common.command.PaymentFailedCommand;
import com.sagapattern.common.command.PaymentSucceededCommand;
import com.sagapattern.common.constant.RabbitMQConstants;
import com.sagapattern.orderservice.entity.Inbox;
import com.sagapattern.orderservice.repository.InboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
@RabbitListener(queues = RabbitMQConstants.PAYMENT_QUEUE)
public class PaymentConsumer {
    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;

    public PaymentConsumer(InboxRepository inboxRepository, ObjectMapper objectMapper) {
        this.inboxRepository = inboxRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitHandler
    public void handlePaymentSuccess(PaymentSucceededCommand command) throws JsonProcessingException {
        handlePaymentEvent(command, PaymentSucceededCommand.class, command.getIdempotentToken());
    }

    @RabbitHandler
    public void handlePaymentFailed(PaymentFailedCommand command) throws JsonProcessingException {
        handlePaymentEvent(command, PaymentFailedCommand.class, command.getIdempotentToken());
    }


    public <T> void handlePaymentEvent(T command, Class<T> type, UUID idempotentToken) throws JsonProcessingException {
        if (inboxRepository.existsById(idempotentToken)) {
            log.info("Event already received. Skipping processing.");
            return;
        }

        Inbox inbox = new Inbox(
                idempotentToken,
                objectMapper.writeValueAsString(command),
                type.getTypeName(),
                false,
                LocalDateTime.now()
        );
        inboxRepository.save(inbox);
    }

}
