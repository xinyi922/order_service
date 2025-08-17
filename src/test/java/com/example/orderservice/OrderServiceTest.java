package com.example.orderservice;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @Test
    void testCreateOrder() {
        OrderRepository repo = new OrderRepository();
        OrderService service = new OrderService(repo);

        OrderDto dto1 = new OrderDto();
        dto1.setItems(Collections.singletonList(1L));
        dto1.setQuantity(2); // 假设OrderDto有这个字段

        Order order = service.createOrder(dto1);
        assertNotNull(order.getOrderId());
        assertEquals("CREATED", order.getStatus());
    }
}
