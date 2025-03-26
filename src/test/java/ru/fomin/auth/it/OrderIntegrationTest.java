package ru.fomin.auth.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.fomin.auth.exception.OrderNotFoundException;
import ru.fomin.auth.model.OrderRequest;
import ru.fomin.auth.model.OrderResponse;
import ru.fomin.auth.persistance.entity.Order;
import ru.fomin.auth.persistance.repository.GoodsRepository;
import ru.fomin.auth.persistance.repository.OrderLineRepository;
import ru.fomin.auth.persistance.repository.OrderRepository;
import ru.fomin.auth.util.DataUtilOrder;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @BeforeEach
    public void setup() {
        orderLineRepository.deleteAll(); // Сначала удаляем OrderLine
        orderRepository.deleteAll();     // Затем Order
        goodsRepository.deleteAll();     // И Goods
    }

    @Test
    @DisplayName("Test create order functionality")
    @WithMockUser(authorities = "write")
    public void givenOrderToCreate_whenCreateOrder_thenCreateOrder() throws Exception {
        //given
        OrderRequest orderRequest = DataUtilOrder.getOrderRequest1();
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client", CoreMatchers.is("Danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date")
                        .value(LocalDate.of(2000, 1, 1).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is("Space")));
    }

    @Test
    @DisplayName("Test update order functionality")
    @WithMockUser(authorities = "write")
    public void givenOrderToUpdate_whenUpdateOrder_thenUpdateOrder() throws Exception {
        //given
        Order order = DataUtilOrder.getOrder1();
        orderRepository.save(order);
        OrderRequest orderRequest = DataUtilOrder.getOrderRequest1().setId(order.getId());
        orderRequest.setAddress("SpaceNew");
        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client", CoreMatchers.is("Danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date")
                        .value(LocalDate.of(2000, 1, 1).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is("SpaceNew")));
    }

    @Test
    @DisplayName("Test update order functionality when order not found")
    @WithMockUser(authorities = "write")
    public void givenOrderNotFound_whenUpdateOrder_thenNotUpdateOrder() throws Exception {
        //given
        OrderRequest orderRequest = DataUtilOrder.getOrderRequest1().setId(1L);
        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Order not found")));
    }

    @Test
    @DisplayName("Test find by id order functionality")
    @WithMockUser(authorities = "read")
    public void givenOrderFindById_whenFindOrderById_thenFindOrderById() throws Exception {
        //given
        Order order = DataUtilOrder.getOrder1();
        orderRepository.save(order);
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/order/" + order.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client", CoreMatchers.is("Danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date")
                        .value(LocalDate.of(2000, 1, 1).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is("Space")));
    }

    @Test
    @DisplayName("Test find by id order functionality when order not found")
    @WithMockUser(authorities = "read")
    public void givenOrderFindById_whenFindOrderById_thenNotFindOrderById() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/order/" + 1)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Order not found")));
    }

    @Test
    @DisplayName("Test find all orders functionality")
    @WithMockUser(authorities = "read")
    public void givenThreeOrders_whenFindAll_thenFindAllOrders() throws Exception {
        //given
        Order order1 = DataUtilOrder.getOrder1();
        Order order2 = DataUtilOrder.getOrder2();
        Order order3 = DataUtilOrder.getOrder3();
        List<Order> order = List.of(order1, order2, order3);
        orderRepository.saveAll(order);
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test delete order functionality")
    @WithMockUser(authorities = "write")
    public void givenOrderDelete_whenDeleteOrder_thenDeleteOrder() throws Exception {
        //given
        Order order = DataUtilOrder.getOrder1();
        orderRepository.save(order);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/order/" + order.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());


    }

    @Test
    @DisplayName("Test delete order functionality when order not found")
    @WithMockUser(authorities = "write")
    public void givenOrderDelete_whenDeleteOrder_thenNotDeleteOrder() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/order/" + 1)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Order not found")));
    }

}
