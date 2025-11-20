package com.terra.terraPizza.Bussines;

import com.terra.terraPizza.DataAcces.OrderItemResponse;
import com.terra.terraPizza.DataAcces.OrderResponse;
import com.terra.terraPizza.Entities.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(OrderItemResponse::new)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getStatus(),
                items
        );
    }
}