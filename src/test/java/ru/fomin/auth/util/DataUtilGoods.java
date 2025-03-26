package ru.fomin.auth.util;

import ru.fomin.auth.model.GoodsRequest;
import ru.fomin.auth.model.GoodsResponse;
import ru.fomin.auth.persistance.entity.Goods;

public class DataUtilGoods {

    public static Goods getGoods1(){
        return Goods.builder()
                .name("mobile")
                .price(10000L)
                .build();
    }

    public static Goods getGoods2(){
        return Goods.builder()
                .name("table")
                .price(20000L)
                .build();
    }

    public static Goods getGoods3(){
        return Goods.builder()
                .name("apple")
                .price(500L)
                .build();
    }
    ///////////////////////////////////////////////////////////////////
    public static GoodsResponse getGoodsResponse1(){
        return GoodsResponse.builder()
                .id(1L)
                .name("mobile")
                .price(10000L)
                .build();
    }

    public static GoodsResponse getGoodsResponse2(){
        return GoodsResponse.builder()
                .id(2L)
                .name("table")
                .price(20000L)
                .build();
    }

    public static GoodsResponse getGoodsResponse3(){
        return GoodsResponse.builder()
                .id(3L)
                .name("apple")
                .price(500L)
                .build();
    }
    //////////////////////////////////////////////////////////////////////
    public static GoodsRequest getGoodsRequest1(){
        return GoodsRequest.builder()
                .name("mobile")
                .price(10000L)
                .build();
    }

    public static GoodsRequest getGoodsRequest2(){
        return GoodsRequest.builder()
                .name("table")
                .price(20000L)
                .build();
    }

    public static GoodsRequest getGoodsRequest3(){
        return GoodsRequest.builder()
                .name("apple")
                .price(500L)
                .build();
    }

}
