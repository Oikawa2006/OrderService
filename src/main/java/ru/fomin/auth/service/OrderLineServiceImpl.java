package ru.fomin.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fomin.auth.exception.OrderLineNotFoundException;
import ru.fomin.auth.mapper.OrderLineMapper;
import ru.fomin.auth.model.OrderLineRequest;
import ru.fomin.auth.model.OrderLineResponse;
import ru.fomin.auth.persistance.entity.OrderLine;
import ru.fomin.auth.persistance.repository.OrderLineRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineMapper orderLineMapper;

    private final OrderLineRepository orderLineRepository;

    @Override
    @Transactional
    public OrderLineResponse create(OrderLineRequest orderLineRequest) {
        return orderLineMapper.map(orderLineRepository.save(orderLineMapper.map(orderLineRequest)));
    }

    @Override
    public OrderLineResponse update(OrderLineRequest orderLineRequest) {
        boolean exist = orderLineRepository.existsById(orderLineRequest.getId());
        if (!exist) {
            throw new OrderLineNotFoundException("OrderLine not found");
        }
        return orderLineMapper.map(orderLineRepository.save(orderLineMapper.map(orderLineRequest)));
    }

    @Override
    public List<OrderLineResponse> findAll() {
        return orderLineMapper.map(orderLineRepository.findAll());
    }

    @Override
    public OrderLineResponse findById(Long id) {
        return orderLineMapper.map(orderLineRepository.findById(id).orElseThrow(() -> new OrderLineNotFoundException("OrderLine not found")));
    }

    @Override
    public void deleteById(Long id) {
        OrderLine orderLine = orderLineRepository.findById(id)
                .orElseThrow(() -> new OrderLineNotFoundException("OrderLine not found"));
        orderLineRepository.deleteById(orderLine.getId());
    }

}
