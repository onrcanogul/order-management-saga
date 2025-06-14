package com.sagapattern.paymentservice.service.payment;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentService {

    public boolean handlePayment() {
        // business
        Random rand = new Random();
        return rand.nextBoolean();
    }
}
