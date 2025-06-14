package com.sagapattern.common.event;

import com.sagapattern.common.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class OrderCreatedEvent {
    private UUID idempotentToken;
    private UUID orderId;
    private double totalPrice;
    private OrderStatus orderStatus;
}
