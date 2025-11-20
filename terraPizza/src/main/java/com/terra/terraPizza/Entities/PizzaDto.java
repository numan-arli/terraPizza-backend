package com.terra.terraPizza.Entities;

public class PizzaDto {
    private int id;
    private String pizzaName;
    private String description;
    private int price;
    private int categoryId;
    private String categoryName;

    public PizzaDto(Pizza pizza) {
        this.id = pizza.getId();
        this.pizzaName = pizza.getName();
        this.description = pizza.getDescription();
        this.price = pizza.getPrice();
        this.categoryId = pizza.getCategory().getId();
        this.categoryName = pizza.getCategory().categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
