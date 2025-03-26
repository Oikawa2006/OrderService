package ru.fomin.auth.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fomin.auth.exception.OrderNotFoundException;
import ru.fomin.auth.mapper.OrderMapper;
import ru.fomin.auth.model.OrderRequest;
import ru.fomin.auth.model.OrderResponse;
import ru.fomin.auth.persistance.entity.Order;
import ru.fomin.auth.persistance.repository.OrderRepository;
import ru.fomin.auth.util.DataUtilOrder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Test save order functionality")
    public void givenOrderToSave_whenSaveOrder_thenOrderIsSaved(){
        //given
        OrderRequest orderRequest = DataUtilOrder.getOrderRequest1();
        Mockito.when(orderMapper.map(any(OrderRequest.class))).thenReturn(DataUtilOrder.getOrder1());
        Mockito.when(orderRepository.save(any(Order.class))).thenReturn(DataUtilOrder.getOrder1().setId(1L));
        Mockito.when(orderMapper.map(any(Order.class))).thenReturn(DataUtilOrder.GetOrderResponse1());
        //when
        OrderResponse response = orderService.saveOrder(orderRequest);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getClient());
        Assertions.assertNotNull(response.getDate());
        Assertions.assertNotNull(response.getAddress());
        Mockito.verify(orderMapper,Mockito.times(1)).map(any(OrderRequest.class));
        Mockito.verify(orderRepository,Mockito.times(1)).save(any(Order.class));
        Mockito.verify(orderMapper,Mockito.times(1)).map(any(Order.class));
    }

    @Test
    @DisplayName("Test update order functionality")
    public void givenOrderToUpdate_whenUpdateOrder_thenOrderIsUpdated(){
        //given
        Order order = DataUtilOrder.getOrder1().setId(1L);
        OrderRequest orderRequest = DataUtilOrder.getOrderRequest1().setId(1L);
        Mockito.lenient().when(orderMapper.map(any(OrderRequest.class))).thenReturn(DataUtilOrder.getOrder1().setId(1L));
        Mockito.lenient().when(orderRepository.save(any(Order.class))).thenReturn(DataUtilOrder.getOrder1().setId(1L));
        Mockito.lenient().when(orderMapper.map(any(Order.class))).thenReturn(DataUtilOrder.GetOrderResponse1());
        Mockito.when(orderRepository.existsById(anyLong())).thenReturn(true);
        //when
        OrderResponse response = orderService.updateOrder(orderRequest);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getClient());
        Assertions.assertNotNull(response.getDate());
        Assertions.assertNotNull(response.getAddress());
        Mockito.verify(orderMapper,Mockito.times(1)).map(any(OrderRequest.class));
        Mockito.verify(orderRepository,Mockito.times(1)).save(any(Order.class));
        Mockito.verify(orderMapper,Mockito.times(1)).map(any(Order.class));
        Mockito.verify(orderRepository,Mockito.times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("Test update order functionality when order not found")
    public void givenOrderToUpdate_whenUpdateOrder_thenOrderNotFoundException(){
        //given
        Order order = DataUtilOrder.getOrder1().setId(1L);
        OrderRequest orderRequest = DataUtilOrder.getOrderRequest1().setId(1L);
        Mockito.lenient().when(orderMapper.map(any(OrderRequest.class))).thenReturn(DataUtilOrder.getOrder1().setId(1L));
        Mockito.lenient().when(orderRepository.save(any(Order.class))).thenReturn(DataUtilOrder.getOrder1().setId(1L));
        Mockito.lenient().when(orderMapper.map(any(Order.class))).thenReturn(DataUtilOrder.GetOrderResponse1());
        Mockito.when(orderRepository.existsById(anyLong())).thenReturn(false);
        //when
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.updateOrder(orderRequest));
        //then
        Mockito.verify(orderMapper,Mockito.never()).map(any(OrderRequest.class));
        Mockito.verify(orderRepository,Mockito.never()).save(any(Order.class));
        Mockito.verify(orderMapper,Mockito.never()).map(any(Order.class));
        Mockito.verify(orderRepository,Mockito.times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("Test find by id order functionality")
    public void givenOrderToFindById_whenFindById_thenOrderIsReturned(){
        //given
        Order order = DataUtilOrder.getOrder1().setId(1L);
        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.lenient().when(orderMapper.map(any(Order.class))).thenReturn(DataUtilOrder.GetOrderResponse1());
        //when
        OrderResponse response = orderService.findById(order.getId());
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getClient());
        Assertions.assertNotNull(response.getDate());
        Assertions.assertNotNull(response.getAddress());
        Mockito.verify(orderRepository,Mockito.times(1)).findById(anyLong());
        Mockito.verify(orderMapper,Mockito.times(1)).map(any(Order.class));
    }

    @Test
    @DisplayName("Test find by id order functionality when order not found")
    public void givenOrderToFindById_whenFindById_thenOrderNotFoundException(){
        //given
        Order order = DataUtilOrder.getOrder1().setId(1L);
        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        Mockito.lenient().when(orderMapper.map(any(Order.class))).thenReturn(DataUtilOrder.GetOrderResponse1());
        //when
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.findById(order.getId()));
        //then
        Mockito.verify(orderRepository,Mockito.times(1)).findById(anyLong());
        Mockito.verify(orderMapper,Mockito.never()).map(any(Order.class));
    }

    @Test
    @DisplayName("Test find all orders functionality")
    public void givenThreeOrders_whenFindAllOrders_thenOrdersAreReturned(){
        //given
        Order order1 = DataUtilOrder.getOrder1().setId(1L);
        Order order2 = DataUtilOrder.getOrder2().setId(2L);
        Order order3 = DataUtilOrder.getOrder3().setId(3L);
        List<Order> orders = List.of(order1,order2,order3);
        OrderResponse orderResponse1 = DataUtilOrder.GetOrderResponse1();
        OrderResponse orderResponse2 = DataUtilOrder.GetOrderResponse2();
        OrderResponse orderResponse3 = DataUtilOrder.GetOrderResponse3();
        List<OrderResponse> orderResponses = List.of(orderResponse1,orderResponse2,orderResponse3);
        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        Mockito.when(orderMapper.map(orders)).thenReturn(orderResponses);
        //when
        List<OrderResponse> responses = orderService.findAll();
        //then
        Assertions.assertNotNull(responses);
        Mockito.verify(orderRepository,Mockito.times(1)).findAll();
        Mockito.verify(orderMapper,Mockito.times(1)).map(orders);
    }

    @Test
    @DisplayName("Test delete order functionality")
    public void givenOrderToDelete_whenDeleteOrder_thenOrderIsDeleted(){
        //given
        Order order = DataUtilOrder.getOrder1().setId(1L);
        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        //when
        orderService.deleteOrderById(order.getId());
        //
        Mockito.verify(orderRepository,Mockito.times(1)).deleteById(anyLong());
        Mockito.verify(orderRepository,Mockito.times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Test delete order functionality when order not found")
    public void givenOrderToDelete_whenDeleteOrder_thenOrderNotFoundException(){
        //given
        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrderById(anyLong()));
        //
        Mockito.verify(orderRepository,Mockito.never()).deleteById(anyLong());
        Mockito.verify(orderRepository,Mockito.times(1)).findById(anyLong());
    }


}
