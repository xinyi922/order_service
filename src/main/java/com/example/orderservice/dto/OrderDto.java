package com.example.orderservice.dto;

import java.util.List;

public class OrderDto {
    private List<Long> items;
    private int quantity;

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
