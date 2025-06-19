package com.sagapattern.common.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.sagapattern.common.constant.RabbitMQConstants.*;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public TopicExchange dlqExchange() {
        return new TopicExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    public Binding globalDlqBinding() {
        return BindingBuilder.bind(dlqQueue())
                .to(dlqExchange())
                .with(DEAD_LETTER_ROUTING_KEY);
    }

    // ------------------------------------------------------------------------------

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderCreatedBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ORDER_CREATED_ROUTING_KEY);
    }

    // ------------------------------------------------------------------------------

    @Bean
    public Queue paymentRequestQueue() {
        return QueueBuilder.durable(PAYMENT_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public TopicExchange paymentRequestExchange() {
        return new TopicExchange(PAYMENT_REQUEST_EXCHANGE);
    }

    @Bean
    public Binding paymentRequestBinding() {
        return BindingBuilder.bind(paymentRequestQueue()).to(paymentRequestExchange()).with(PAYMENT_REQUEST_ROUTING_KEY);
    }


    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, true);
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Binding paymentSuccessBinding() {
        return BindingBuilder.bind(paymentQueue()).to(paymentExchange()).with(PAYMENT_SUCCESS_ROUTING_KEY);
    }

    @Bean
    public Binding paymentFailedBinding() {
        return BindingBuilder.bind(paymentQueue()).to(paymentExchange()).with(PAYMENT_FAILED_ROUTING_KEY);
    }



}