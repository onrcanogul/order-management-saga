package com.sagapattern.sagaorchestrator.saga.listener;

import com.sagapattern.common.constant.RabbitMQConstants;
import com.sagapattern.common.event.PaymentFailedEvent;
import com.sagapattern.sagaorchestrator.saga.manager.SagaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentFailedListener {
    private final SagaManager sagaManager;

    public PaymentFailedListener(SagaManager sagaManager) {
        this.sagaManager = sagaManager;
    }

    @RabbitListener(queues = RabbitMQConstants.PAYMENT_QUEUE)
    public void handlePaymentSucceededEvent(PaymentFailedEvent event) {
        sagaManager.handlePaymentFailedEvent(event);
    }
}
