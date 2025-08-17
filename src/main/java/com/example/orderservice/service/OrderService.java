package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository repo;

    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    public Order createOrder(OrderDto dto) {
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new IllegalArgumentException("No items in order");
        }

        // 简化逻辑：默认取第一个 itemId
        Long itemId = dto.getItems().get(0);
        Order order = new Order();
        order.setItemId(itemId);
        order.setQuantity(dto.getQuantity());
        order.setStatus("NEW");

        return repo.save(order);
    }

    public Optional<Order> getOrder(Long id) {
        return repo.findById(id);
    }

    // 故意抛出异常，测试事务回滚
    @Transactional
    public void createOrderWithError() {
        Order order = new Order();
        order.setItemId(1L);
        order.setQuantity(10);
//        repo.save(order);
        throw new RuntimeException("Simulated failure"); // 测试事务回滚
    }

    public Optional<Order> findById(Long id) {
        return repo.findById(id);
    }
}
