package ru.fomin.auth.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.fomin.auth.model.OrderLineRequest;
import ru.fomin.auth.model.OrderLineResponse;
import ru.fomin.auth.persistance.entity.OrderLine;

import java.util.List;

@Mapper
@Component
public interface OrderLineMapper {

    OrderLineResponse map(OrderLine orderLine);

    List<OrderLineResponse> map(List<OrderLine> orderLines);

    OrderLine map(OrderLineRequest orderLineRequest);

}
