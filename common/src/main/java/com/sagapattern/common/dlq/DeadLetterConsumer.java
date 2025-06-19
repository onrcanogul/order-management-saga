package com.sagapattern.common.dlq;

import com.sagapattern.common.constant.RabbitMQConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DeadLetterConsumer {
    private final RabbitTemplate rabbitTemplate;

    public DeadLetterConsumer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConstants.DEAD_LETTER_QUEUE)
    public void handleDlq(Message message) {
        List<Map<String, Object>> deaths = (List<Map<String, Object>>) message.getMessageProperties().getHeaders().get("x-death");

        if (deaths != null && !deaths.isEmpty()) {
            Map<String, Object> death = deaths.get(0);
            String originalExchange = (String) death.get("exchange");
            List<String> routingKeys = (List<String>) death.get("routing-keys");
            String originalRoutingKey = routingKeys.get(0);
            String originalQueue = (String) death.get("queue");

            log.error("DLQ Message from Exchange: {}, RoutingKey: {}, Queue: {}",
                    originalExchange, originalRoutingKey, originalQueue);
        }

        log.error("Payload: {}", new String(message.getBody()));
    }
}
