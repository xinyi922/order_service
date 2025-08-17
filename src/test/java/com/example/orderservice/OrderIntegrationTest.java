package com.example.orderservice;

import com.example.orderservice.model.Order;
//import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfig.class // 使用专用测试配置
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderIntegrationTestWithoutDocker {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setupDatabase() {
        // 手动初始化表结构
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // 创建 orders 表
        jdbcTemplate.execute("DROP TABLE IF EXISTS order_items");
        jdbcTemplate.execute("DROP TABLE IF EXISTS orders");
        jdbcTemplate.execute("CREATE TABLE orders (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "customer VARCHAR(255) NOT NULL" +
                ")");

        // 创建 order_items 表
        jdbcTemplate.execute("CREATE TABLE order_items (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "order_id BIGINT NOT NULL, " +
                "sku VARCHAR(50) NOT NULL, " +
                "qty INT NOT NULL, " +
                "FOREIGN KEY (order_id) REFERENCES orders(id)" +
                ")");
    }

    @Test
    void createOrder_persists() throws Exception {
        String body = "{\"customer\":\"c1\",\"items\":[{\"sku\":\"A1\",\"qty\":1}]}";

        mvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        assertThat(orderRepository.findAll()).hasSize(1);
    }

    @Test
    void createOrderWithError_shouldRollback() throws Exception {
        String body = "{\"customer\":\"c2\",\"items\":[{\"sku\":\"B1\",\"qty\":2}]}";

        mvc.perform(post("/orders/error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isInternalServerError());

        assertThat(orderRepository.findAll()).isEmpty();
    }
}