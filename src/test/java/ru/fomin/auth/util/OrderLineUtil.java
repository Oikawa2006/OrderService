package ru.fomin.auth.util;

import ru.fomin.auth.model.*;
import ru.fomin.auth.persistance.entity.Goods;
import ru.fomin.auth.persistance.entity.Order;
import ru.fomin.auth.persistance.entity.OrderLine;

import java.time.LocalDate;

public class OrderLineUtil {

    public static OrderLine getOrderLine1() {
        Order order = Order.builder() // Новый объект Order
                .client("Danil")
                .date(LocalDate.of(2000, 1, 1))
                .address("Space")
                .build();

        Goods goods = Goods.builder() // Новый объект Goods
                .name("Table")
                .price(10000L)
                .build();

        return OrderLine.builder()
                .count(5L)
                .order(order)
                .goods(goods)
                .build();
    }

    public static OrderLine getOrderLine2() {
        Order order = Order.builder() // Новый объект Order
                .client("Danil")
                .date(LocalDate.of(2000, 1, 1))
                .address("Space")
                .build();

        Goods goods = Goods.builder() // Новый объект Goods
                .name("Table")
                .price(10000L)
                .build();

        return OrderLine.builder()
                .order(order)
                .goods(goods)
                .count(2L)
                .build();
    }

    ///
    static OrderResponse orderResponse = OrderResponse.builder()
            .client("Danil")
            .date(LocalDate.of(2000, 1, 1))
            .address("Space")
            .build();

    static GoodsResponse goodsResponse = GoodsResponse.builder()
            .name("Table")
            .price(10000L)
            .build();

    public static OrderLineResponse getOrderLineResponse1() {
        return OrderLineResponse.builder()
                .id(1L)
                .order(orderResponse)
                .goods(goodsResponse)
                .count(5L)
                .build();
    }

    public static OrderLineResponse getOrderLineResponse2() {
        return OrderLineResponse.builder()
                .id(2L)
                .order(orderResponse)
                .goods(goodsResponse)
                .count(2L)
                .build();
    }

    ///
    static OrderRequest orderRequest = OrderRequest.builder()
            .client("Danil")
            .date(LocalDate.of(2000, 1, 1))
            .address("Space")
            .build();

    static GoodsRequest goodsRequest = GoodsRequest.builder()
            .name("Table")
            .price(10000L)
            .build();

    public static OrderLineRequest getOrderLineRequest1() {
        return OrderLineRequest.builder()
                .order(orderRequest)
                .goods(goodsRequest)
                .count(5)
                .build();
    }

    public static OrderLineRequest getOrderLineRequest2() {
        return OrderLineRequest.builder()
                .order(orderRequest)
                .goods(goodsRequest)
                .count(2L)
                .build();
    }

}
