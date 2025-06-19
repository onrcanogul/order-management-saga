package com.sagapattern.common.event;

import com.sagapattern.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
public class OrderCreatedEvent implements Serializable {
    private UUID idempotentToken;
    private UUID orderId;
    private double totalPrice;
    private OrderStatus orderStatus;
}
