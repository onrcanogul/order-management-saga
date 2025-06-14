package com.sagapattern.sagaorchestrator.saga.listener;

import com.sagapattern.common.command.PaymentRequestCommand;
import com.sagapattern.common.constant.RabbitMQConstants;
import com.sagapattern.common.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderCreatedListener {
    private final RabbitTemplate rabbitTemplate;

    public OrderCreatedListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConstants.ORDER_QUEUE)
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        PaymentRequestCommand paymentRequestCommand = PaymentRequestCommand.builder()
                .orderId(event.getOrderId())
                .orderStatus(event.getOrderStatus())
                .idempotentToken(event.getIdempotentToken())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConstants.PAYMENT_REQUEST_EXCHANGE, RabbitMQConstants.PAYMENT_REQUEST_ROUTING_KEY, paymentRequestCommand);
    }
}
