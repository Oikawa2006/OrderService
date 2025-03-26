package ru.fomin.auth.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.fomin.auth.model.OrderLineRequest;
import ru.fomin.auth.persistance.entity.OrderLine;
import ru.fomin.auth.persistance.repository.GoodsRepository;
import ru.fomin.auth.persistance.repository.OrderLineRepository;
import ru.fomin.auth.persistance.repository.OrderRepository;
import ru.fomin.auth.util.OrderLineUtil;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OrderLineIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @BeforeEach
    public void setup() {
        orderLineRepository.deleteAll(); // Сначала удаляем OrderLine
        orderRepository.deleteAll();     // Затем Order
        goodsRepository.deleteAll();     // И Goods
    }

    @Test
    @DisplayName("Test create orderLine functionality")
    @WithMockUser(authorities = "write")
    public void givenOrderLineToCreate_whenCreateOrderLine_thenOk() throws Exception {
        //given
        OrderLineRequest orderLineRequest = OrderLineUtil.getOrderLineRequest1();
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
    @WithMockUser(authorities = "write")
    public void givenOrderLineToUpdate_whenUpdateOrderLine_thenOk() throws Exception {
        //given
        OrderLine orderLine = OrderLineUtil.getOrderLine1();
        orderLineRepository.save(orderLine);
        OrderLineRequest orderLineRequest = OrderLineUtil.getOrderLineRequest1().setId(orderLine.getId());
        orderLineRequest.setCount(4L);
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.count", CoreMatchers.is(4)));
    }

    @Test
    @DisplayName("Test update orderLine functionality not found")
    @WithMockUser(authorities = "write")
    public void givenOrderLineToUpdate_whenUpdateOrderLine_thenBad() throws Exception {
        //given
        OrderLineRequest orderLineRequest = OrderLineUtil.getOrderLineRequest1().setId(1L);
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
    @WithMockUser(authorities = "read")
    public void giverOrderLine_whenFindById_thenOk() throws Exception {
        //given
        OrderLine orderLine = OrderLineUtil.getOrderLine1();
        orderLineRepository.save(orderLine);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/orderLine/" + orderLine.getId())
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
    @WithMockUser(authorities = "read")
    public void givenOrderLine_whenFindById_thenBad() throws Exception {
        //given

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
    @WithMockUser(authorities = "read")
    public void givenTwoOrderLine_whenFindAll_thenIsOk() throws Exception {
        //given
        OrderLine orderLine1 = OrderLineUtil.getOrderLine1();
        OrderLine orderLine2 = OrderLineUtil.getOrderLine2();
        List<OrderLine> orderLines = List.of(orderLine1, orderLine2);
        orderLineRepository.saveAll(orderLines);


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
    @WithMockUser(authorities = "write")
    public void givenOrderLineToDelete_whenDelete_thenIsOk() throws Exception {
        //given
        OrderLine orderLine = OrderLineUtil.getOrderLine1();
        orderLineRepository.save(orderLine);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/orderLine/" + orderLine.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test delete functionality when orderLine not found")
    @WithMockUser(authorities = "write")
    public void givenOrderLineToDelete_whenDelete_thenIsBad() throws Exception {
        //given;
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
