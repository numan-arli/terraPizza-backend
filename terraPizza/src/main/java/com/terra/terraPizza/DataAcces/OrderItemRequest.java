package com.terra.terraPizza.DataAcces;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderItemRequest {
    private Long productId;
    private int quantity;
    private int totalPrice;
    private boolean extraMozzarella;
    private List<String> toppings = new ArrayList<>();
    private String description;

    public void setExtraMozzarella(boolean extraMozzarella) {
        this.extraMozzarella = extraMozzarella;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExtraMozzarella() {
        return extraMozzarella;
    }

    public void extraMozzarella(boolean extraMozzarella) {
        this.extraMozzarella = extraMozzarella;
    }

    public List<String> getToppings() {
        return toppings;
    }

    public void setToppings(List<String> toppings) {
        this.toppings = toppings;
    }

    public BigDecimal getTotalPrice() {
        return BigDecimal.valueOf(totalPrice);
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

