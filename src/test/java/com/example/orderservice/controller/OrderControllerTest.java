package com.example.orderservice.controller;

import com.example.orderservice.OrderServiceApplication;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@ComponentScan(basePackages = "com.example.orderservice")
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @Test
    void getOrder_ok() throws Exception {
        Order order = new Order();
        order.setOrderId(1L);
        order.setItemId(100L);
        order.setQuantity(2);
        order.setStatus("NEW");

        when(orderService.getOrder(1L)).thenReturn(Optional.of(order));

        mvc.perform(get("/api/orders/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void getOrder_notFound() throws Exception {
        when(orderService.getOrder(999L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/orders/999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOrder_ok() throws Exception {
        Order order = new Order();
        order.setOrderId(1L);
        order.setItemId(100L);
        order.setQuantity(2);
        order.setStatus("NEW");

        when(orderService.createOrder(any(OrderDto.class))).thenReturn(order);

        String json = """
            {
              "items": [
                { "itemId": 100, "quantity": 2 }
              ]
            }
            """;

        mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.status").value("NEW"));
    }
}
