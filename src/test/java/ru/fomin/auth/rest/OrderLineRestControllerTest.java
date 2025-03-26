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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.fomin.auth.exception.OrderLineNotFoundException;
import ru.fomin.auth.model.OrderLineRequest;
import ru.fomin.auth.model.OrderLineResponse;
import ru.fomin.auth.persistance.entity.OrderLine;
import ru.fomin.auth.security.JwtTokenProvider;
import ru.fomin.auth.service.OrderLineService;
import ru.fomin.auth.util.OrderLineUtil;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(value = OrderLineRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderLineRestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderLineService orderLineService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("Test create orderLine functionality")
    public void givenOrderLineToCreate_whenCreateOrderLine_thenOk() throws Exception {
        //given
        OrderLineRequest orderLineRequest = OrderLineUtil.getOrderLineRequest1();
        Mockito.when(orderLineService.create(any(OrderLineRequest.class))).thenReturn(OrderLineUtil.getOrderLineResponse1());
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/orderLine")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderLineRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.order.client").value(orderLineRequest.getOrder().getClient()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.order.date").value(orderLineRequest.getOrder().getDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.order.address").value(orderLineRequest.getOrder().getAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goods.name").value("Table"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goods.price").value(10000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count", CoreMatchers.is(5)));
    }

    @Test
    @DisplayName("Test update orderLine functionality")
    public void givenOrderLineToUpdate_whenUpdateOrderLine_thenOk() throws Exception {
        //given
        OrderLineRequest orderLineRequest = OrderLineUtil.getOrderLineRequest1().setId(1L);
        OrderLine orderLine = OrderLineUtil.getOrderLine1().setId(1L);
        Mockito.when(orderLineService.update(any(OrderLineRequest.class))).thenReturn(OrderLineUtil.getOrderLineResponse1());
        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/orderLine")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderLineRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.order.client").value(orderLineRequest.getOrder().getClient()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.order.date").value(orderLineRequest.getOrder().getDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.order.address", CoreMatchers.is(orderLineRequest.getOrder().getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goods.name").value("Table"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goods.price").value(10000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count", CoreMatchers.is(5)));
    }

    @Test
    @DisplayName("Test update orderLine functionality not found")
    public void givenOrderLineToUpdate_whenUpdateOrderLine_thenBad() throws Exception {
        //given
        OrderLineRequest orderLineRequest = OrderLineUtil.getOrderLineRequest1().setId(1L);
        OrderLine orderLine = OrderLineUtil.getOrderLine1().setId(1L);
        Mockito.when(orderLineService.update(any(OrderLineRequest.class))).thenThrow(new OrderLineNotFoundException("OrderLine not found"));
        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/orderLine")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderLineRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("OrderLine not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)));
    }

    @Test
    @DisplayName("Test find by id functionality")
    public void giverOrderLine_whenFindById_thenOk() throws Exception {
        //given
        OrderLine orderLine = OrderLineUtil.getOrderLine1().setId(1L);
        Mockito.when(orderLineService.findById(anyLong())).thenReturn(OrderLineUtil.getOrderLineResponse1());
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/orderLine/"+orderLine.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.order.client").value(orderLine.getOrder().getClient()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.order.date").value(orderLine.getOrder().getDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.order.address", CoreMatchers.is(orderLine.getOrder().getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goods.name").value("Table"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goods.price").value(10000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count", CoreMatchers.is(5)));
    }

    @Test
    @DisplayName("Test find by id functionality when orderLine not found")
    public void givenOrderLine_whenFindById_thenBad() throws Exception {
        //given
        Mockito.when(orderLineService.findById(anyLong())).thenThrow(new OrderLineNotFoundException("OrderLine not found"));
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/orderLine/" + 1L)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("OrderLine not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)));
    }

    @Test
    @DisplayName("Test find all functionality")
    public void givenTwoOrderLine_whenFindAll_thenIsOk() throws Exception {
        //given
        OrderLine orderLine1 = OrderLineUtil.getOrderLine1().setId(1L);
        OrderLine orderLine2 = OrderLineUtil.getOrderLine2().setId(2L);
        List<OrderLine> orderLines = List.of(orderLine1, orderLine2);
        OrderLineResponse orderLineResponse1 = OrderLineUtil.getOrderLineResponse1();
        OrderLineResponse orderLineResponse2 = OrderLineUtil.getOrderLineResponse2();
        List<OrderLineResponse> orderLineResponses = List.of(orderLineResponse1, orderLineResponse2);
        Mockito.when(orderLineService.findAll()).thenReturn(orderLineResponses);
        //then
        ResultActions resultActions = mockMvc.perform(get("/api/v1/orderLine")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test delete functionality")
    public void givenOrderLineToDelete_whenDelete_thenIsOk() throws Exception {
        //given
        OrderLine orderLine = OrderLineUtil.getOrderLine1().setId(1L);
        Mockito.doNothing().when(orderLineService).deleteById(anyLong());
        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/orderLine/" + orderLine.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(orderLineService, Mockito.times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test delete functionality when orderLine not found")
    public void givenOrderLineToDelete_whenDelete_thenIsBad() throws Exception {
        //given;
        Mockito.doThrow(new OrderLineNotFoundException("OrderLine not found")).when(orderLineService).deleteById(anyLong());
        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/orderLine/" + 1L)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("OrderLine not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)));
    }


}
