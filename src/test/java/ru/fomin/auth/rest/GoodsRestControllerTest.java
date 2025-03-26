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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.fomin.auth.exception.GoodsNotFoundException;
import ru.fomin.auth.model.GoodsRequest;
import ru.fomin.auth.model.GoodsResponse;
import ru.fomin.auth.persistance.entity.Goods;
import ru.fomin.auth.security.JwtTokenProvider;
import ru.fomin.auth.service.GoodsService;
import ru.fomin.auth.util.DataUtilGoods;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(value = GoodsRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GoodsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GoodsService goodsService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("Test create goods functionality")

    public void givenGoodsToSave_whenCreateGoods_thenSuccess() throws Exception {
        //given
        GoodsRequest goodsRequest = DataUtilGoods.getGoodsRequest1();
        Mockito.when(goodsService.saveGoods(any(GoodsRequest.class))).thenReturn(DataUtilGoods.getGoodsResponse1());
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
    public void givenGoodsToUpdate_whenUpdateGoods_thenSuccess() throws Exception {
        //given
        GoodsRequest goodsRequest = DataUtilGoods.getGoodsRequest1().setId(1L);
        Mockito.when(goodsService.updateGoods(any(GoodsRequest.class))).thenReturn(DataUtilGoods.getGoodsResponse1());
        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/goods")
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
    @DisplayName("Test update goods functionality when goods not found")
    public void givenGoodsToUpdate_whenUpdateGoods_thenNotSuccess() throws Exception {
        //given
        GoodsRequest goodsRequest = DataUtilGoods.getGoodsRequest1().setId(1L);
        Mockito.when(goodsService.updateGoods(any(GoodsRequest.class))).thenThrow(new GoodsNotFoundException("Goods not found"));
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
    public void givenGoodsFindById_whenFindGoods_thenSuccess() throws Exception {
        //given
        Goods goods = DataUtilGoods.getGoods1().setId(1L);
        Mockito.when(goodsService.findGoods(anyLong())).thenReturn(DataUtilGoods.getGoodsResponse1());
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
    public void givenGoodsFindById_whenFindGoods_thenNotSuccess() throws Exception {
        //given
        Goods goods = DataUtilGoods.getGoods1().setId(1L);
        Mockito.when(goodsService.findGoods(anyLong())).thenThrow(new GoodsNotFoundException("Goods not found"));
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/goods/" + goods.getId())
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
    public void givenGoodsFindAll_whenFindAllGoods_thenSuccess() throws Exception {
        //given
        Goods goods1 = DataUtilGoods.getGoods1().setId(1L);
        Goods goods2 = DataUtilGoods.getGoods2().setId(2L);
        Goods goods3 = DataUtilGoods.getGoods3().setId(3L);
        List<Goods> goods = List.of(goods1, goods2, goods3);
        GoodsResponse goodsResponse1 = DataUtilGoods.getGoodsResponse1();
        GoodsResponse goodsResponse2 = DataUtilGoods.getGoodsResponse2();
        GoodsResponse goodsResponse3 = DataUtilGoods.getGoodsResponse3();
        List<GoodsResponse> goodsResponses = List.of(goodsResponse1, goodsResponse2, goodsResponse3);
        Mockito.when(goodsService.findAllGoods()).thenReturn(goodsResponses);
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
    public void givenGoodsToDelete_whenDeleteGoods_thenSuccess() throws Exception {
        //given
        Goods goods = DataUtilGoods.getGoods1().setId(1L);
        Mockito.doNothing().when(goodsService).delete(anyLong());
        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/goods/" + goods.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(goodsService, Mockito.times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Test delete goods functionality when goods not found")
    public void givenGoodsToDelete_whenDeleteGoods_thenNotSuccess() throws Exception {
        //given
        Mockito.doThrow(new GoodsNotFoundException("Goods not found")).when(goodsService).delete(anyLong());
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
