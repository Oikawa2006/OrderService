package ru.fomin.auth.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.fomin.auth.persistance.entity.Goods;
import ru.fomin.auth.persistance.repository.GoodsRepository;
import ru.fomin.auth.util.DataUtilGoods;

import java.util.List;

@DataJpaTest
public class GoodsRepositoryTest {

    @Autowired
    private  GoodsRepository goodsRepository;

    @BeforeEach
    public void setUp(){
        goodsRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save goods functionality")
    public void givenGoodsToSave_whenSaveGoods_thenSuccess(){
        //given
        Goods goods = DataUtilGoods.getGoods1();
        //when
        Goods savedGoods = goodsRepository.save(goods);
        //then
        Assertions.assertNotNull(savedGoods);
        Assertions.assertNotNull(savedGoods.getId());
        Assertions.assertNotNull(savedGoods.getName());
        Assertions.assertNotNull(savedGoods.getPrice());
    }

    @Test
    @DisplayName("Test update goods functionality")
    public void givenGoodsToUpdate_whenUpdateGoods_thenSuccess(){
        //given
        long updatedPrice  = 250L;
        Goods goods = DataUtilGoods.getGoods1();
        goodsRepository.save(goods);
        //when
        Goods goodsToUpdate = goodsRepository.findById(goods.getId())
                .orElse(null);
        goodsToUpdate.setPrice(updatedPrice);
        goodsRepository.save(goodsToUpdate);
        //then
        Assertions.assertNotNull(goodsToUpdate);
        Assertions.assertNotNull(goodsToUpdate.getId());
        Assertions.assertNotNull(goodsToUpdate.getName());
        Assertions.assertNotNull(goodsToUpdate.getPrice());
        Assertions.assertEquals(updatedPrice, goodsToUpdate.getPrice());
    }

    @Test
    @DisplayName("Test find by id goods functionality")
    public void givenGoodsToFindById_whenFindGoods_thenSuccess(){
        //given
        Goods goods = DataUtilGoods.getGoods1();
        goodsRepository.save(goods);
        //when
        Goods foundGoods = goodsRepository.findById(goods.getId())
                .orElse(null);
        //then
        Assertions.assertNotNull(foundGoods);
        Assertions.assertNotNull(foundGoods.getId());
        Assertions.assertNotNull(foundGoods.getName());
        Assertions.assertNotNull(foundGoods.getPrice());
    }

    @Test
    @DisplayName("Test find by id goods functionality when goods not found")
    public void givenGoodsToFindById_whenFindGoods_thenNotSuccess(){
        //given
        //when
        Goods foundGoods = goodsRepository.findById(-1L)
                .orElse(null);
        //then
        Assertions.assertNull(foundGoods);
    }

    @Test
    @DisplayName("Test find all goods functionality")
    public void givenThreeGoods_whenFindAll_thenSuccess(){
        //when
        Goods goods1 = DataUtilGoods.getGoods1();
        Goods goods2 = DataUtilGoods.getGoods2();
        Goods goods3 = DataUtilGoods.getGoods3();
        List<Goods> goods = List.of(goods1,goods2,goods3);
        goodsRepository.saveAll(goods);
        //when
        List<Goods> foundGoods = goodsRepository.findAll();
        //then
        Assertions.assertNotNull(foundGoods);
        Assertions.assertEquals(3, foundGoods.size());
    }

    @Test
    @DisplayName("Test delete goods functionality")
    public void givenGoodsToDelete_whenDeleteGoods_thenSuccess(){
        //given
        Goods goods = DataUtilGoods.getGoods1();
        goodsRepository.save(goods);
        //when
        goodsRepository.delete(goods);
        //then
        Goods foundGoods = goodsRepository.findById(goods.getId())
                .orElse(null);
        Assertions.assertNull(foundGoods);

    }

}
