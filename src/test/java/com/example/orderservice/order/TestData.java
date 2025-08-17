package com.example.orderservice.order;

import com.example.orderservice.dto.OrderDto;

import java.util.Collections;

public class TestData {
    public static OrderDto sampleOrderDto() {
        OrderDto dto = new OrderDto();
        dto.setItems(Collections.singletonList(100L)); // 随便一个 itemId
        dto.setQuantity(2);
        return dto;
    }
}
