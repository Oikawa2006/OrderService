package ru.fomin.auth.service;

import ru.fomin.auth.model.GoodsRequest;
import ru.fomin.auth.model.GoodsResponse;

import java.util.List;

public interface GoodsService {

    GoodsResponse saveGoods(GoodsRequest goodsRequest);

    GoodsResponse updateGoods(GoodsRequest goodsRequest);

    GoodsResponse findGoods(Long id);

    List<GoodsResponse> findAllGoods();

    void delete(Long id);


}
