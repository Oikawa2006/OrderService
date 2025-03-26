package ru.fomin.auth.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.fomin.auth.model.OrderRequest;
import ru.fomin.auth.model.OrderResponse;
import ru.fomin.auth.persistance.entity.Order;

import java.util.List;

@Mapper
@Component
public interface OrderMapper {

    OrderResponse map(Order order);

    List<OrderResponse> map(List<Order> orders);

    Order map(OrderRequest orderRequest);

}
