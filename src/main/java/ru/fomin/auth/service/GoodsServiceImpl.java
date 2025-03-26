package ru.fomin.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fomin.auth.exception.GoodsNotFoundException;
import ru.fomin.auth.mapper.GoodsMapper;
import ru.fomin.auth.model.GoodsRequest;
import ru.fomin.auth.model.GoodsResponse;
import ru.fomin.auth.persistance.entity.Goods;
import ru.fomin.auth.persistance.repository.GoodsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;

    private final GoodsRepository goodsRepository;

    @Override
    public GoodsResponse saveGoods(GoodsRequest goodsRequest) {
        return goodsMapper.map(goodsRepository.save(goodsMapper.map(goodsRequest)));
    }

    @Override
    public GoodsResponse updateGoods(GoodsRequest goodsRequest) {
        boolean isExist = goodsRepository.existsById(goodsRequest.getId());
        if (!isExist) {
            throw new GoodsNotFoundException("Goods not found");
        }
        return goodsMapper.map(goodsRepository.save(goodsMapper.map(goodsRequest)));
    }

    @Override
    public GoodsResponse findGoods(Long id) {
        return goodsMapper.map(goodsRepository.findById(id)
                .orElseThrow(() -> new GoodsNotFoundException("Goods not found")));
    }

    @Override
    public List<GoodsResponse> findAllGoods() {
        return goodsMapper.map(goodsRepository.findAll());
    }

    @Override
    public void delete(Long id) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(() -> new GoodsNotFoundException("Goods not found"));
        goodsRepository.deleteById(goods.getId());
    }

}
