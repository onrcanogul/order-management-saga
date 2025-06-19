package com.sagapattern.common.configuration;

import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;

public class RabbitMQGeneralConfiguration {
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        factory.setAdviceChain(
                RetryInterceptorBuilder.stateless()
                        .maxAttempts(5)
                        .backOffOptions(1000, 2.0, 10000)
                        .build()
        );

        factory.setDefaultRequeueRejected(false);  // DLQ devreye girsin diye
        return factory;
    }
}
