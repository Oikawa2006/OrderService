package ru.fomin.auth.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.fomin.auth.exception.OrderNotFoundException;
import ru.fomin.auth.model.OrderRequest;
import ru.fomin.auth.model.OrderResponse;
import ru.fomin.auth.persistance.entity.Order;
import ru.fomin.auth.security.JwtTokenProvider;
import ru.fomin.auth.security.SecurityUser;
import ru.fomin.auth.security.UserDetailsServiceImpl;
import ru.fomin.auth.service.OrderService;
import ru.fomin.auth.util.DataUtilOrder;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(value = OrderRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("Test create order functionality")
    public void givenOrderToCreate_whenCreateOrder_thenCreateOrder() throws Exception {
        //given
        OrderRequest orderRequest = DataUtilOrder.getOrderRequest1();
        Mockito.when(orderService.saveOrder(any(OrderRequest.class))).thenReturn(DataUtilOrder.GetOrderResponse1());
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
    public void givenOrderToUpdate_whenUpdateOrder_thenUpdateOrder() throws Exception {
        //given
        OrderRequest orderRequest = DataUtilOrder.getOrderRequest1().setId(1L);
        Mockito.when(orderService.updateOrder(any(OrderRequest.class))).thenReturn(DataUtilOrder.GetOrderResponse1());
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is("Space")));
    }

    @Test
    @DisplayName("Test update order functionality when order not found")
    public void givenOrderNotFound_whenUpdateOrder_thenNotUpdateOrder() throws Exception {
        //given
        OrderRequest orderRequest = DataUtilOrder.getOrderRequest1().setId(1L);
        Mockito.when(orderService.updateOrder(any(OrderRequest.class)))
                .thenThrow(new OrderNotFoundException("Order not found"));
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
    public void givenOrderFindById_whenFindOrderById_thenFindOrderById() throws Exception {
        //given
        Order order = DataUtilOrder.getOrder1().setId(1L);
        Mockito.when(orderService.findById(anyLong())).thenReturn(DataUtilOrder.GetOrderResponse1());
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/order/"+order.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client",CoreMatchers.is("Danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date")
                        .value(LocalDate.of(2000, 1, 1).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is("Space")));
    }

    @Test
    @DisplayName("Test find by id order functionality when order not found")
    public void givenOrderFindById_whenFindOrderById_thenNotFindOrderById() throws Exception {
        //given
        Mockito.when(orderService.findById(anyLong())).thenThrow(new OrderNotFoundException("Order not found"));
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/order/"+1)
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
    public void givenThreeOrders_whenFindAll_thenFindAllOrders() throws Exception {
        //given
        Order order1 = DataUtilOrder.getOrder1().setId(1L);
        Order order2 = DataUtilOrder.getOrder2().setId(2L);
        Order order3 = DataUtilOrder.getOrder3().setId(3L);
        List<Order> order = List.of(order1,order2,order3);
        OrderResponse orderResponse1 = DataUtilOrder.GetOrderResponse1();
        OrderResponse orderResponse2 = DataUtilOrder.GetOrderResponse2();
        OrderResponse orderResponse3 = DataUtilOrder.GetOrderResponse3();
        List<OrderResponse> orderResponses = List.of(orderResponse1,orderResponse2,orderResponse3);
        Mockito.when(orderService.findAll()).thenReturn(orderResponses);
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test delete order functionality")
    public void givenOrderDelete_whenDeleteOrder_thenDeleteOrder() throws Exception {
        //given
        Order order = DataUtilOrder.getOrder1().setId(1L);
        Mockito.doNothing().when(orderService).deleteOrderById(anyLong());
        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/order/"+order.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(orderService, Mockito.times(1)).deleteOrderById(anyLong());

    }

    @Test
    @DisplayName("Test delete order functionality when order not found")
    public void givenOrderDelete_whenDeleteOrder_thenNotDeleteOrder() throws Exception {
        //given
        Mockito.doThrow(new OrderNotFoundException("Order not found")).when(orderService).deleteOrderById(anyLong());
        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/order/"+1)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Order not found")));
    }


}
