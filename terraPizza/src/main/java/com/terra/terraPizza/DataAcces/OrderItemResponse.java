package com.terra.terraPizza.DataAcces;

import com.terra.terraPizza.Entities.OrderItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class OrderItemResponse {
    private String name;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private boolean extraMozzarella;
    private List<String> toppings;
    private String description;

    public OrderItemResponse(OrderItem item) {
        this.name = item.getProduct().getName();
        this.quantity = item.getQuantity();
        this.unitPrice = item.getUnitPrice();
        this.totalPrice = item.getTotalPrice();
        this.extraMozzarella = item.isExtraMozzarella();
        this.description = item.getDescription();

        // Toppings JSON string -> List<String>
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (item.getToppings() != null && !item.getToppings().isEmpty()) {
                this.toppings = mapper.readValue(item.getToppings(), new TypeReference<List<String>>() {});
            } else {
                this.toppings = Collections.emptyList();
            }
        } catch (Exception e) {
            this.toppings = Collections.emptyList();
        }
    }

    // Getters
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public boolean isExtraMozzarella() { return extraMozzarella; }
    public List<String> getToppings() { return toppings; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setToppings(List<String> toppings) {
        this.toppings = toppings;
    }

    public void setExtraMozzarella(boolean extraMozzarella) {
        this.extraMozzarella = extraMozzarella;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setName(String name) {
        this.name = name;
    }
}
