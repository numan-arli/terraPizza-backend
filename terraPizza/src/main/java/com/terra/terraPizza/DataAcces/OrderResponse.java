package com.terra.terraPizza.DataAcces;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private String status;
    private List<OrderItemResponse> items;

    public OrderResponse(Long orderId, LocalDateTime orderDate, BigDecimal totalPrice, String status, List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.items = items;
    }

    // Getters
    public Long getOrderId() { return orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public List<OrderItemResponse> getItems() { return items; }
}
