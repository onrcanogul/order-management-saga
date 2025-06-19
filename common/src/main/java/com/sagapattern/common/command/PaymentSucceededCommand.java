package com.sagapattern.common.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class PaymentSucceededCommand implements Serializable {
    private UUID idempotentToken;
    private UUID orderId;
}
