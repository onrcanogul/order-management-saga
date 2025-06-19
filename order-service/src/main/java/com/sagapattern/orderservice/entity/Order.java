package com.sagapattern.orderservice.entity;

import com.sagapattern.common.entity.BaseEntity;
import com.sagapattern.common.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Order extends BaseEntity {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    private OrderStatus status;
    private double totalPrice = orderItems != null ? orderItems.stream().mapToDouble(OrderItem::getPrice).sum() : 0;
}
