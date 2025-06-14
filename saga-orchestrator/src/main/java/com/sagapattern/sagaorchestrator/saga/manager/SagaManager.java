package com.sagapattern.sagaorchestrator.saga.manager;

import com.sagapattern.common.command.PaymentFailedCommand;
import com.sagapattern.common.command.PaymentRequestCommand;
import com.sagapattern.common.command.PaymentSucceededCommand;
import com.sagapattern.common.event.OrderCreatedEvent;
import com.sagapattern.common.constant.RabbitMQConstants;
import com.sagapattern.common.event.PaymentFailedEvent;
import com.sagapattern.common.event.PaymentSucceededEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SagaManager {

    private final RabbitTemplate rabbitTemplate;

    public SagaManager(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("SagaManager is processing OrderCreatedEvent for orderId {}", event.getOrderId());

        PaymentRequestCommand command = PaymentRequestCommand.builder()
                .orderId(event.getOrderId())
                .orderStatus(event.getOrderStatus())
                .idempotentToken(event.getIdempotentToken())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConstants.PAYMENT_REQUEST_EXCHANGE,
                RabbitMQConstants.PAYMENT_REQUEST_ROUTING_KEY,
                command
        );

        log.info("SagaManager sent PaymentRequestCommand for orderId {}", event.getOrderId());
    }

    public void handlePaymentFailedEvent(PaymentFailedEvent event) {
        PaymentFailedCommand paymentFailedCommand =  PaymentFailedCommand.builder()
                .orderId(event.getOrderId())
                .idempotentToken(event.getIdempotentToken())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConstants.PAYMENT_EXCHANGE, RabbitMQConstants.PAYMENT_FAILED_ROUTING_KEY, paymentFailedCommand);
    }

    public void handlePaymentSucceededEvent(PaymentSucceededEvent event) {
        PaymentSucceededCommand paymentSucceededCommand =  PaymentSucceededCommand.builder()
                .orderId(event.getOrderId())
                .idempotentToken(event.getIdempotentToken())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConstants.PAYMENT_EXCHANGE, RabbitMQConstants.PAYMENT_SUCCESS_ROUTING_KEY, paymentSucceededCommand);
    }
}
