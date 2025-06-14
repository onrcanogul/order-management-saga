package com.sagapattern.orderservice.service.order;

import com.sagapattern.common.constant.RabbitMQConstants;
import com.sagapattern.common.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateOrderEvent(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.ORDER_EXCHANGE,
                RabbitMQConstants.ORDER_CREATED_ROUTING_KEY,
                event);
    }
}
