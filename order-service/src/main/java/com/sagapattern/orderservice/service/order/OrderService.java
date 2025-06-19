package com.sagapattern.orderservice.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapattern.common.enums.OrderStatus;
import com.sagapattern.common.event.OrderCreatedEvent;
import com.sagapattern.orderservice.entity.Order;
import com.sagapattern.orderservice.entity.Outbox;
import com.sagapattern.orderservice.repository.OrderRepository;
import com.sagapattern.orderservice.repository.OutboxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OrderService(
            OrderRepository orderRepository,
            OutboxRepository outboxRepository,
            ObjectMapper objectMapper
    ) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void createOrder(Order order) throws JsonProcessingException {
        order.getOrderItems().forEach(oi -> oi.setId(UUID.randomUUID()));
        UUID orderId = UUID.randomUUID();
        order.setId(orderId);
        orderRepository.save(order);
        outboxRepository.save(convertToOutbox(order));
    }

    public void updateStatus(OrderStatus status, UUID orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order Not Found"));
        order.setStatus(status);
    }

    private Outbox convertToOutbox(Order order) throws JsonProcessingException {
        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .idempotentToken(UUID.randomUUID()).orderId(order.getId()).orderStatus(order.getStatus()).totalPrice(order.getTotalPrice())
                .build();
        Outbox outbox = new Outbox();
        outbox.setPayload(objectMapper.writeValueAsString(orderCreatedEvent));
        outbox.setPublished(false);
        outbox.setAggregateType(Order.class.getTypeName());
        outbox.setType(OrderCreatedEvent.class.getTypeName());
        return outbox;
    }
}
