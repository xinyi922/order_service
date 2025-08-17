//package com.example.orderservice;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.example.orderservice.model.Order;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.notNullValue;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class OrderControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper mapper;
//
////    @Test
////    void testCreateAndGetOrder() throws Exception {
////        Order req = new Order(null, 1L, 2, null);
////        mockMvc.perform(post("/orders")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(mapper.writeValueAsString(req)))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.orderId").exists());
////    }
//
//    @Test
//    void testCreateOrderSuccess() throws Exception {
//        Order order = new Order(null, 1L, 2, "CREATED");
//
//        mockMvc.perform(post("/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(order)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.orderId", notNullValue()))
//                .andExpect(jsonPath("$.itemId", is(1)))
//                .andExpect(jsonPath("$.quantity", is(2)))
//                .andExpect(jsonPath("$.status", is("CREATED")));
//    }
//
//    @Test
//    void testGetOrderByIdSuccess() throws Exception {
//        // 先创建
//        String resp = mockMvc.perform(post("/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(new Order(null, 2L, 5, "CREATED"))))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//        Order created = mapper.readValue(resp, Order.class);
//
//        // 再查询
//        mockMvc.perform(get("/orders/{id}", created.getOrderId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.itemId", is(2)))
//                .andExpect(jsonPath("$.quantity", is(5)))
//                .andExpect(jsonPath("$.status", is("CREATED")));
//    }
//
//    @Test
//    void testGetOrderByIdNotFound() throws Exception {
//        mockMvc.perform(get("/orders/{id}", 999))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testGetAllOrders() throws Exception {
//        mockMvc.perform(post("/orders")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(new Order(null, 3L, 1, "CREATED"))));
//        mockMvc.perform(post("/orders")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(new Order(null, 4L, 10, "CREATED"))));
//
//        mockMvc.perform(get("/orders"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)));
//    }
//
//    @Test
//    void testUpdateOrderSuccess() throws Exception {
//        String resp = mockMvc.perform(post("/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(new Order(null, 5L, 3, "CREATED"))))
//                .andReturn().getResponse().getContentAsString();
//        Order created = mapper.readValue(resp, Order.class);
//
//        Order updated = new Order(created.getOrderId(), 5L, 8, "UPDATED");
//
//        mockMvc.perform(put("/orders/{id}", created.getOrderId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(updated)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.quantity", is(8)))
//                .andExpect(jsonPath("$.status", is("UPDATED")));
//    }
//
//    @Test
//    void testUpdateOrderNotFound() throws Exception {
//        Order updated = new Order(999L, 10L, 5, "UPDATED");
//
//        mockMvc.perform(put("/orders/{id}", 999)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(updated)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testDeleteOrderSuccess() throws Exception {
//        String resp = mockMvc.perform(post("/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(new Order(null, 6L, 2, "CREATED"))))
//                .andReturn().getResponse().getContentAsString();
//        Order created = mapper.readValue(resp, Order.class);
//
//        mockMvc.perform(delete("/orders/{id}", created.getOrderId()))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testDeleteOrderNotFound() throws Exception {
//        mockMvc.perform(delete("/orders/{id}", 999))
//                .andExpect(status().isNotFound());
//    }
//}
package com.example.orderservice;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.findAll().forEach(order -> orderRepository.delete(order.getOrderId()));
        orderRepository.save(new Order(null, 1L, 2, "NEW"));
        orderRepository.save(new Order(null, 2L, 5, "NEW"));
    }


    @Test
    void testCreateOrderSuccess() throws Exception {
        Order order = new Order(null, 1L, 2, null); // status 交给后端默认赋值

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", notNullValue()))
                .andExpect(jsonPath("$.itemId", is(1)))
                .andExpect(jsonPath("$.quantity", is(2)))
                .andExpect(jsonPath("$.status", is("NEW"))); // 后端默认 NEW
    }

    @Test
    void testGetOrderByIdSuccess() throws Exception {
        // 先创建
        String resp = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Order(null, 2L, 5, null))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Order created = mapper.readValue(resp, Order.class);

        // 再查询
        mockMvc.perform(get("/api/orders/{id}", created.getOrderId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId", is(2)))
                .andExpect(jsonPath("$.quantity", is(5)))
                .andExpect(jsonPath("$.status", is("NEW")));
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/orders/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))  // 确认返回 2 个订单
                .andExpect(jsonPath("$[0].status", is("NEW")))
                .andExpect(jsonPath("$[1].status", is("NEW")));
    }

    @Test
    void testUpdateOrderSuccess() throws Exception {
        String resp = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Order(null, 5L, 3, null))))
                .andReturn().getResponse().getContentAsString();
        Order created = mapper.readValue(resp, Order.class);

        Order updated = new Order(created.getOrderId(), 5L, 8, "UPDATED");

        mockMvc.perform(put("/api/orders/{id}", created.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(8)))
                .andExpect(jsonPath("$.status", is("UPDATED")));
    }

    @Test
    void testUpdateOrderNotFound() throws Exception {
        Order updated = new Order(999L, 10L, 5, "UPDATED");

        mockMvc.perform(put("/api/orders/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteOrderSuccess() throws Exception {
        String resp = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Order(null, 6L, 2, null))))
                .andReturn().getResponse().getContentAsString();
        Order created = mapper.readValue(resp, Order.class);

        mockMvc.perform(delete("/api/orders/{id}", created.getOrderId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteOrderNotFound() throws Exception {
        mockMvc.perform(delete("/api/orders/{id}", 999))
                .andExpect(status().isNotFound());
    }
}
