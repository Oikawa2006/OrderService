package ru.fomin.auth.service;

import ru.fomin.auth.model.OrderRequest;
import ru.fomin.auth.model.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse saveOrder(OrderRequest orderRequest);

    OrderResponse updateOrder(OrderRequest orderRequest);

    OrderResponse findById(Long id);

    List<OrderResponse> findAll();

    void deleteOrderById(Long id);

}
