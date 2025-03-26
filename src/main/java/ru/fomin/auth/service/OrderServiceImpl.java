package ru.fomin.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fomin.auth.exception.OrderNotFoundException;
import ru.fomin.auth.mapper.OrderMapper;
import ru.fomin.auth.model.OrderRequest;
import ru.fomin.auth.model.OrderResponse;
import ru.fomin.auth.persistance.entity.Order;
import ru.fomin.auth.persistance.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    @Override
    public OrderResponse saveOrder(OrderRequest orderRequest) {
        return orderMapper.map(orderRepository.save(orderMapper.map(orderRequest)));
    }

    @Override
    public OrderResponse updateOrder(OrderRequest orderRequest) {
        boolean isExist = orderRepository.existsById(orderRequest.getId());
        if (!isExist) {
            throw new OrderNotFoundException("Order not found");
        }
        return orderMapper.map(orderRepository.save(orderMapper.map(orderRequest)));
    }

    @Override
    public OrderResponse findById(Long id) {
        return orderMapper.map(orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found")));
    }

    @Override
    public List<OrderResponse> findAll() {
        return orderMapper.map(orderRepository.findAll());
    }

    @Override
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        orderRepository.deleteById(order.getId());
    }
}
