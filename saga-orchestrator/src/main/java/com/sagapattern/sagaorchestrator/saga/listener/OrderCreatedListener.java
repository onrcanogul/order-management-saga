package com.sagapattern.sagaorchestrator.saga.listener;

import com.sagapattern.common.constant.RabbitMQConstants;
import com.sagapattern.common.event.OrderCreatedEvent;
import com.sagapattern.sagaorchestrator.saga.manager.SagaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = RabbitMQConstants.ORDER_QUEUE)
public class OrderCreatedListener {
    private final SagaManager sagaManager;

    public OrderCreatedListener(SagaManager sagaManager) {
        this.sagaManager = sagaManager;
    }

    @RabbitHandler
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        sagaManager.handleOrderCreated(event);
    }
}
