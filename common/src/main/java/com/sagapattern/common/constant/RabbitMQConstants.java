package com.sagapattern.common.constant;

public class RabbitMQConstants {
    public static final String DEAD_LETTER_QUEUE = "dead-letter-queue";
    public static final String DEAD_LETTER_EXCHANGE = "dead-letter-exchange";
    public static final String DEAD_LETTER_ROUTING_KEY = "dead-letter-routing-key";

    public static final String ORDER_QUEUE = "order-queue";
    public static final String ORDER_EXCHANGE = "order-exchange";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    public static final String PAYMENT_REQUEST_QUEUE = "payment-request-queue";
    public static final String PAYMENT_REQUEST_EXCHANGE = "payment-request-exchange";
    public static final String PAYMENT_REQUEST_ROUTING_KEY = "payment.request";

    public static final String PAYMENT_QUEUE = "payment-queue";
    public static final String PAYMENT_EXCHANGE =  "payment-exchange";
    public static final String PAYMENT_SUCCESS_ROUTING_KEY = "payment.success";
    public static final String PAYMENT_FAILED_ROUTING_KEY = "payment.failed";
}
