package com.example.orderservice;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.dto.OrderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void cleanUp() {
        // 清空内存仓库，保证每个测试独立
        orderRepository.deleteAll();
    }

    @Test
    void testCreateSingleOrder() {
        OrderDto dto2 = new OrderDto();
        dto2.setItems(Collections.singletonList(1L));
        dto2.setQuantity(5);

        Order order = orderService.createOrder(dto2);
        assertThat(order.getOrderId()).isNotNull();

        List<Order> allOrders = orderRepository.findAll();
        assertThat(allOrders.size()).isEqualTo(1);
        assertThat(allOrders.get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    void testCreateMultipleOrders() {
        OrderDto dto3 = new OrderDto();
        dto3.setItems(Collections.singletonList(1L));
        dto3.setQuantity(5);
        orderService.createOrder(dto3);
        OrderDto dto4 = new OrderDto();
        dto4.setItems(Collections.singletonList(2L));
        dto4.setQuantity(10);
        orderService.createOrder(dto4);
        OrderDto dto5 = new OrderDto();
        dto5.setItems(Collections.singletonList(3L));
        dto5.setQuantity(15);
        orderService.createOrder(dto5);

        List<Order> allOrders = orderRepository.findAll();
        assertThat(allOrders.size()).isEqualTo(3);
        assertThat(allOrders.stream().mapToInt(Order::getQuantity).sum()).isEqualTo(30);
    }

    @Test
    void testCreateOrderWithErrorRollsBack() {
        // 模拟事务回滚：createOrderWithError 方法内部抛异常
        assertThrows(RuntimeException.class, () -> orderService.createOrderWithError());

        // 内存仓库应该为空
        List<Order> allOrders = orderRepository.findAll();
        assertThat(allOrders.size()).isEqualTo(0);
    }

    @Test
    void testDeleteAll() {
        OrderDto dto6 = new OrderDto();
        dto6.setItems(Collections.singletonList(1L));
        dto6.setQuantity(5);
        orderService.createOrder(dto6);
        OrderDto dto7 = new OrderDto();
        dto7.setItems(Collections.singletonList(2L));
        dto7.setQuantity(10);
        orderService.createOrder(dto7);

        assertThat(orderRepository.findAll().size()).isEqualTo(2);

        orderRepository.deleteAll();
        assertThat(orderRepository.findAll().size()).isEqualTo(0);
    }
}
