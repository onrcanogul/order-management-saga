package com.sagapattern.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PaymentFailedEvent {
    private UUID idempotentToken;
    private UUID orderId;
}
