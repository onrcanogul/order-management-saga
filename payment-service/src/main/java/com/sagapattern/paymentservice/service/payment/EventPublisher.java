package com.sagapattern.paymentservice.service.payment;

import com.sagapattern.common.constant.RabbitMQConstants;
import com.sagapattern.common.event.PaymentFailedEvent;
import com.sagapattern.common.event.PaymentSucceededEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPaymentSucceededEvent(PaymentSucceededEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.PAYMENT_EXCHANGE,
                RabbitMQConstants.PAYMENT_SUCCESS_ROUTING_KEY,
                event);
    }

    public void sendPaymentFailedEvent(PaymentFailedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.PAYMENT_EXCHANGE,
                RabbitMQConstants.PAYMENT_FAILED_ROUTING_KEY,
                event);
    }
}
