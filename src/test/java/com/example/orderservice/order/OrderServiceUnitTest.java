//package com.example.order;
//
//import com.example.orderservice.model.Order;
//import com.example.orderservice.repository.OrderRepository;
//import com.example.orderservice.service.OrderService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class OrderServiceUnitTest {
//
//    @Mock
//    private OrderRepository repo;
//
//    @InjectMocks
//    private OrderService service;
//
//    @Test
//    void createOrder_whenQuantityZero_thenThrow() {
//        Order order = new Order(null, 1L, 0, "NEW");
//
//        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
//            service.createOrder(order);
//        });
//        assertEquals("Quantity must be greater than 0", ex.getMessage());
//    }
//
//    @Test
//    void createOrder_whenValid_thenSaved() {
//        Order order = new Order(null, 1L, 2, "NEW");
//        when(repo.save(any())).thenAnswer(inv -> {
//            Order o = inv.getArgument(0);
//            o.setOrderId(99L);
//            return o;
//        });
//
//        Order result = service.createOrder(order);
//
//        assertNotNull(result.getOrderId());
//        assertEquals(99L, result.getOrderId());
//        verify(repo, times(1)).save(any(Order.class));
//    }
//
//    @Test
//    void getOrder_whenExists_thenReturn() {
//        Order order = new Order(1L, 1L, 2, "NEW");
//        when(repo.findById(1L)).thenReturn(Optional.of(order));
//
//        Order result = service.getOrder(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getOrderId());
//        assertEquals("NEW", result.getStatus());
//        verify(repo).findById(1L);
//    }
//
//    @Test
//    void getOrder_whenNotExists_thenThrow() {
//        when(repo.findById(2L)).thenReturn(Optional.empty());
//
//        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> {
//            service.getOrder(2L);
//        });
//        assertEquals("Order not found", ex.getMessage());
//    }
//}
package com.example.orderservice.order;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock
    private OrderRepository repo;

    @InjectMocks
    private OrderService service;

    @Test
    void createOrder_whenNoItems_thenThrow() {
        OrderDto dto = new OrderDto();
        dto.setItems(Collections.emptyList());

        // 断言抛出异常
        assertThrows(IllegalArgumentException.class, () -> {
            service.createOrder(dto);
        });

        // repo 不应该被调用
        verify(repo, never()).save(any());
    }

    @Test
    void createOrder_whenValid_thenSaved() {
        OrderDto dto = TestData.sampleOrderDto();
        when(repo.save(any())).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setOrderId(1L); // 模拟 DB 分配主键
            return o;
        });

        Order result = service.createOrder(dto);

        assertNotNull(result.getOrderId());
        assertEquals("NEW", result.getStatus());
        verify(repo, times(1)).save(any());
    }

    @Test
    void getOrder_whenExists_thenReturn() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setItemId(100L);
        order.setQuantity(2);
        order.setStatus("NEW");

        when(repo.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = service.getOrder(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getOrderId());
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void getOrder_whenNotExists_thenEmpty() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        Optional<Order> result = service.getOrder(999L);

        assertFalse(result.isPresent());
        verify(repo, times(1)).findById(999L);
    }
}
