package com.example.orderservice.repository;

import com.example.orderservice.model.Order;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OrderRepository {
    private final Map<Long, Order> orders = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Order save(Order order) {
        if (order.getOrderId() == null) {
            order.setOrderId(idCounter.getAndIncrement());
        }
        orders.put(order.getOrderId(), order);
        return order;
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public void delete(Long id) {
        orders.remove(id);
    }

    public void deleteAll() {
        orders.clear();
        idCounter.set(1); // 可选：如果希望重置 ID 计数器
    }

}
//package com.example.orderservice.repository;
//
//import com.example.orderservice.model.Order;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface OrderRepository extends JpaRepository<Order, Long> {
//}
