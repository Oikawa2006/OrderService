package ru.fomin.auth.service;

import ru.fomin.auth.model.OrderLineRequest;
import ru.fomin.auth.model.OrderLineResponse;

import java.util.List;

public interface OrderLineService {

    OrderLineResponse create(OrderLineRequest orderLineRequest);

    OrderLineResponse update(OrderLineRequest orderLineRequest);

    List<OrderLineResponse> findAll();

    OrderLineResponse findById(Long id);

    void deleteById(Long id);

}
