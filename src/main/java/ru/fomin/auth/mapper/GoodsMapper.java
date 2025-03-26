package ru.fomin.auth.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.fomin.auth.model.GoodsRequest;
import ru.fomin.auth.model.GoodsResponse;
import ru.fomin.auth.persistance.entity.Goods;

import java.util.List;

@Mapper
@Component
public interface GoodsMapper {

    GoodsResponse map(Goods goods);

    List<GoodsResponse> map(List<Goods> goodsList);

    Goods map(GoodsRequest goodsRequest);

}
