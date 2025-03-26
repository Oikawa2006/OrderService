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
import ru.fomin.auth.model.GoodsRequest;
import ru.fomin.auth.model.GoodsResponse;
import ru.fomin.auth.persistance.entity.Goods;
import ru.fomin.auth.persistance.repository.GoodsRepository;
import ru.fomin.auth.persistance.repository.OrderLineRepository;
import ru.fomin.auth.persistance.repository.OrderRepository;
import ru.fomin.auth.util.DataUtilGoods;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GoodsIntegrationTest {
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
    @DisplayName("Test create goods functionality")
    @WithMockUser(authorities = "write")
    public void givenGoodsToSave_whenCreateGoods_thenSuccess() throws Exception {
        //given
        GoodsRequest goodsRequest = DataUtilGoods.getGoodsRequest1();
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/goods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goodsRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("mobile")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(10000)));
    }

    @Test
    @DisplayName("Test update goods functionality")
    @WithMockUser(authorities = "write")
    public void givenGoodsToUpdate_whenUpdateGoods_thenSuccess() throws Exception {
        //given
        Goods goods = DataUtilGoods.getGoods1();
        goodsRepository.save(goods);
        GoodsRequest goodsRequest = DataUtilGoods.getGoodsRequest1().setId(goods.getId());
        goodsRequest.setName("Update");
        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/goods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goodsRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("Update")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(10000)));

    }

    @Test
    @DisplayName("Test update goods functionality when goods not found")
    @WithMockUser(authorities = "write")
    public void givenGoodsToUpdate_whenUpdateGoods_thenNotSuccess() throws Exception {
        //given
        GoodsRequest goodsRequest = DataUtilGoods.getGoodsRequest1().setId(1L);

        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/goods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goodsRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Goods not found")));
    }

    @Test
    @DisplayName("Test find by id goods functionality")
    @WithMockUser(authorities = "read")
    public void givenGoodsFindById_whenFindGoods_thenSuccess() throws Exception {
        //given
        Goods goods = DataUtilGoods.getGoods1();
        goodsRepository.save(goods);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/goods/{id}", goods.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("mobile")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(10000)));
    }

    @Test
    @DisplayName("Test find by id goods functionality when goods not found")
    @WithMockUser(authorities = "read")
    public void givenGoodsFindById_whenFindGoods_thenNotSuccess() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/goods/" + -1)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Goods not found")));
    }

    @Test
    @DisplayName("Test find all goods functionality")
    @WithMockUser(authorities = "read")
    public void givenGoodsFindAll_whenFindAllGoods_thenSuccess() throws Exception {
        //given
        Goods goods1 = DataUtilGoods.getGoods1();
        Goods goods2 = DataUtilGoods.getGoods2();
        Goods goods3 = DataUtilGoods.getGoods3();
        List<Goods> goods = List.of(goods1, goods2, goods3);
        goodsRepository.saveAll(goods);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/goods")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test delete goods functionality")
    @WithMockUser(authorities = "write")
    public void givenGoodsToDelete_whenDeleteGoods_thenSuccess() throws Exception {
        //given
        Goods goods = DataUtilGoods.getGoods1();
        goodsRepository.save(goods);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/goods/" + goods.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("Test delete goods functionality when goods not found")
    @WithMockUser(authorities = "write")
    public void givenGoodsToDelete_whenDeleteGoods_thenNotSuccess() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/goods/" + -1L)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Goods not found")));
    }


}
