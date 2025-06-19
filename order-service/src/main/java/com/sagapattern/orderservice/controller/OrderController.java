package com.sagapattern.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sagapattern.orderservice.entity.Order;
import com.sagapattern.orderservice.service.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Order order) throws JsonProcessingException {
        orderService.createOrder(order);
        return ResponseEntity.status(201).body(null);
    }
}
