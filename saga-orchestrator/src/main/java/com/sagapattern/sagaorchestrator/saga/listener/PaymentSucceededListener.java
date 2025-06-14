package com.sagapattern.sagaorchestrator.saga.listener;

import com.sagapattern.common.constant.RabbitMQConstants;
import com.sagapattern.common.event.PaymentSucceededEvent;
import com.sagapattern.sagaorchestrator.saga.manager.SagaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentSucceededListener {
    private final SagaManager sagaManager;

    public PaymentSucceededListener(SagaManager sagaManager) {
        this.sagaManager = sagaManager;
    }

    @RabbitListener(queues = RabbitMQConstants.PAYMENT_QUEUE)
    public void handlePaymentSucceededEvent(PaymentSucceededEvent event) {
        sagaManager.handlePaymentSucceededEvent(event);
    }
}
