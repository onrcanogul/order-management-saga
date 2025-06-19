package com.sagapattern.sagaorchestrator.saga.listener;

import com.sagapattern.common.constant.RabbitMQConstants;
import com.sagapattern.common.event.PaymentFailedEvent;
import com.sagapattern.common.event.PaymentSucceededEvent;
import com.sagapattern.sagaorchestrator.saga.manager.SagaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = RabbitMQConstants.PAYMENT_QUEUE)
public class PaymentListener {
    private final SagaManager sagaManager;

    public PaymentListener(SagaManager sagaManager) {
        this.sagaManager = sagaManager;
    }
    @RabbitHandler
    public void handlePaymentFailedEvent(PaymentFailedEvent event) {
        sagaManager.handlePaymentFailedEvent(event);
    }
    @RabbitHandler
    public void handlePaymentSucceededEvent(PaymentSucceededEvent event) {
        sagaManager.handlePaymentSucceededEvent(event);
    }
}
