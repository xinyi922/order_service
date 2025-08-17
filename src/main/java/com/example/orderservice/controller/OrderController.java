package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ✅ 创建订单
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // 创建时统一初始化状态为 "NEW"
        if (order.getStatus() == null) {
            order.setStatus("NEW");
        }
        Order saved = orderRepository.save(order);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/error")
    public Order createOrderWithError(@RequestBody Order order) {
        orderRepository.save(order); // 先保存
        throw new RuntimeException("Forced error for rollback test"); // 强制抛出异常触发回滚
    }

    // ✅ 查询所有订单
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ✅ 按 ID 查询订单
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 更新订单
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setItemId(updatedOrder.getItemId());
                    order.setQuantity(updatedOrder.getQuantity());
                    order.setStatus(updatedOrder.getStatus());
                    orderRepository.save(order);
                    return ResponseEntity.ok(order);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 删除订单
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderRepository.findById(id).isPresent()) {
            orderRepository.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
