package ru.fomin.auth.util;

import ru.fomin.auth.model.OrderRequest;
import ru.fomin.auth.model.OrderResponse;
import ru.fomin.auth.persistance.entity.Order;

import java.time.LocalDate;

public class DataUtilOrder {

    public static Order getOrder1(){
        return Order.builder()
                .client("Danil")
                .date(LocalDate.of(2000,1,1))
                .address("Space")
                .build();
    }

    public static Order getOrder2(){
        return Order.builder()
                .client("Dima")
                .date(LocalDate.of(2002,1,2))
                .address("Apple")
                .build();
    }

    public static Order getOrder3(){
        return Order.builder()
                .client("Oleg")
                .date(LocalDate.of(2005,1,3))
                .address("Mouse")
                .build();
    }
    ////////////////////////////////////////////////////////////////////////////

    public static OrderResponse GetOrderResponse1(){
        return OrderResponse.builder()
                .client("Danil")
                .date(LocalDate.of(2000,1,1))
                .address("Space")
                .id(1L)
                .build();
    }

    public static OrderResponse GetOrderResponse2(){
        return OrderResponse.builder()
                .client("Dima")
                .date(LocalDate.of(2002,1,2))
                .address("Apple")
                .id(2L)
                .build();
    }

    public static OrderResponse GetOrderResponse3(){
        return OrderResponse.builder()
                .client("Oleg")
                .date(LocalDate.of(2005,1,3))
                .address("Mouse")
                .id(3L)
                .build();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public static OrderRequest getOrderRequest1(){
        return OrderRequest.builder()
                .client("Danil")
                .date(LocalDate.of(2000,1,1))
                .address("Space")
                .build();
    }

    public static OrderRequest getOrderRequest2(){
        return OrderRequest.builder()
                .client("Dima")
                .date(LocalDate.of(2002,1,2))
                .address("Apple")
                .build();
    }

    public static OrderRequest getOrderRequest3(){
        return OrderRequest.builder()
                .client("Oleg")
                .date(LocalDate.of(2005,1,3))
                .address("Mouse")
                .build();
    }

}
