package ru.fomin.auth.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fomin.auth.exception.GoodsNotFoundException;
import ru.fomin.auth.mapper.GoodsMapper;
import ru.fomin.auth.model.GoodsRequest;
import ru.fomin.auth.model.GoodsResponse;
import ru.fomin.auth.persistance.entity.Goods;
import ru.fomin.auth.persistance.repository.GoodsRepository;
import ru.fomin.auth.util.DataUtilGoods;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class GoodsServiceImplTest {

    @Mock
    private GoodsRepository goodsRepository;

    @Mock
    private GoodsMapper goodsMapper;

    @InjectMocks
    private GoodsServiceImpl goodsService;

    @Test
    @DisplayName("Test save goods functionality")
    public void givenGoodsToSave_whenSaveGoods_thenSaveGoods() {
        //given
        GoodsRequest goodsRequest = DataUtilGoods.getGoodsRequest1();
        Mockito.when(goodsMapper.map(any(GoodsRequest.class))).thenReturn(DataUtilGoods.getGoods1());
        Mockito.when(goodsRepository.save(any(Goods.class))).thenReturn(DataUtilGoods.getGoods1().setId(1L));
        Mockito.when(goodsMapper.map(any(Goods.class))).thenReturn(DataUtilGoods.getGoodsResponse1());
        //when
        GoodsResponse response = goodsService.saveGoods(goodsRequest);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getName());
        Assertions.assertNotNull(response.getPrice());
        Mockito.verify(goodsMapper,Mockito.times(1)).map(any(GoodsRequest.class));
        Mockito.verify(goodsRepository,Mockito.times(1)).save(any(Goods.class));
        Mockito.verify(goodsMapper,Mockito.times(1)).map(any(Goods.class));
    }

    @Test
    @DisplayName("Test update goods functionality")
    public void givenGoodsToUpdate_whenUpdateGoods_thenUpdateGoods() {
        //given
        GoodsRequest goodsRequest = DataUtilGoods.getGoodsRequest1().setId(1L);
        Mockito.lenient().when(goodsMapper.map(any(GoodsRequest.class))).thenReturn(DataUtilGoods.getGoods1());
        Mockito.lenient().when(goodsRepository.save(any(Goods.class))).thenReturn(DataUtilGoods.getGoods1().setId(1L));
        Mockito.lenient().when(goodsMapper.map(any(Goods.class))).thenReturn(DataUtilGoods.getGoodsResponse1());
        Mockito.when(goodsRepository.existsById(anyLong())).thenReturn(true);
        //when
        GoodsResponse response = goodsService.updateGoods(goodsRequest);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getName());
        Assertions.assertNotNull(response.getPrice());
        Mockito.verify(goodsMapper,Mockito.times(1)).map(any(GoodsRequest.class));
        Mockito.verify(goodsRepository,Mockito.times(1)).save(any(Goods.class));
        Mockito.verify(goodsMapper,Mockito.times(1)).map(any(Goods.class));
        Mockito.verify(goodsRepository,Mockito.times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("Test update goods functionality when goods not found")
    public void givenGoodsToUpdate_whenUpdateGoods_thenNotUpdateGoods() {
        //given
        GoodsRequest goodsRequest = DataUtilGoods.getGoodsRequest1().setId(1L);
        Mockito.lenient().when(goodsMapper.map(any(GoodsRequest.class))).thenReturn(DataUtilGoods.getGoods1());
        Mockito.lenient().when(goodsRepository.save(any(Goods.class))).thenReturn(DataUtilGoods.getGoods1().setId(1L));
        Mockito.lenient().when(goodsMapper.map(any(Goods.class))).thenReturn(DataUtilGoods.getGoodsResponse1());
        Mockito.when(goodsRepository.existsById(anyLong())).thenReturn(false);
        //when
        Assertions.assertThrows(GoodsNotFoundException.class,()->goodsService.updateGoods(goodsRequest));
        //then

        Mockito.verify(goodsMapper,Mockito.never()).map(any(GoodsRequest.class));
        Mockito.verify(goodsRepository,Mockito.never()).save(any(Goods.class));
        Mockito.verify(goodsMapper,Mockito.never()).map(any(Goods.class));
        Mockito.verify(goodsRepository,Mockito.times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("Test find by id goods functionality ")
    public void givenGoodsFindById_whenFindGoods_thenFindGoods() {
        //given
        Goods goods = DataUtilGoods.getGoods1().setId(1L);
        Mockito.lenient().when(goodsMapper.map(any(Goods.class))).thenReturn(DataUtilGoods.getGoodsResponse1());
        Mockito.when(goodsRepository.findById(anyLong())).thenReturn(Optional.of(goods));
        //when
        GoodsResponse response = goodsService.findGoods(goods.getId());
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getName());
        Assertions.assertNotNull(response.getPrice());
        Mockito.verify(goodsMapper,Mockito.times(1)).map(any(Goods.class));
        Mockito.verify(goodsRepository,Mockito.times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Test find by id goods functionality when goods not found ")
    public void givenGoodsFindById_whenFindGoods_thenNotFindGoods() {
        //given
        Goods goods = DataUtilGoods.getGoods1().setId(1L);
        Mockito.lenient().when(goodsMapper.map(any(Goods.class))).thenReturn(DataUtilGoods.getGoodsResponse1());
        Mockito.when(goodsRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        Assertions.assertThrows(GoodsNotFoundException.class,()->goodsService.findGoods(goods.getId()));
        //then
        Mockito.verify(goodsMapper,Mockito.never()).map(any(Goods.class));
        Mockito.verify(goodsRepository,Mockito.times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Test find all goods functionality")
    public void givenThreeGoods_whenFindAllGoods_thenFindAllGoods() {
        Goods goods1 = DataUtilGoods.getGoods1().setId(1L);
        Goods goods2 = DataUtilGoods.getGoods2().setId(2L);
        Goods goods3 = DataUtilGoods.getGoods3().setId(3L);
        List<Goods> goods = List.of(goods1,goods2,goods3);
        GoodsResponse goodsResponse1 = DataUtilGoods.getGoodsResponse1();
        GoodsResponse goodsResponse2 = DataUtilGoods.getGoodsResponse2();
        GoodsResponse goodsResponse3 = DataUtilGoods.getGoodsResponse3();
        List<GoodsResponse> goodsResponses = List.of(goodsResponse1,goodsResponse2,goodsResponse3);
        Mockito.when(goodsRepository.findAll()).thenReturn(goods);
        Mockito.when(goodsMapper.map(goods)).thenReturn(goodsResponses);
        //when
        List<GoodsResponse> response = goodsService.findAllGoods();
        //then
        Assertions.assertNotNull(response);
        Mockito.verify(goodsRepository,Mockito.times(1)).findAll();
        Mockito.verify(goodsMapper,Mockito.times(1)).map(goods);
    }

    @Test
    @DisplayName("Test delete goods functionality")
    public void givenGoodsToDelete_whenDeleteGoods_thenDeleteGoods() {
        //given
        Goods goods = DataUtilGoods.getGoods1().setId(1L);
        Mockito.when(goodsRepository.findById(anyLong())).thenReturn(Optional.of(goods));
        //when
        goodsService.delete(goods.getId());
        //then
        Mockito.verify(goodsRepository,Mockito.times(1)).deleteById(anyLong());
        Mockito.verify(goodsRepository,Mockito.times((1))).findById(anyLong());
    }

    @Test
    @DisplayName("Test delete goods functionality when goods not found")
    public void givenGoodsToDelete_whenDeleteGoods_thenNotDeleteGoods() {
        //given
        Mockito.when(goodsRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        Assertions.assertThrows(GoodsNotFoundException.class,()->goodsService.delete(-1L));
        //then
        Mockito.verify(goodsRepository,Mockito.never()).deleteById(anyLong());
        Mockito.verify(goodsRepository,Mockito.times((1))).findById(anyLong());
    }


}
