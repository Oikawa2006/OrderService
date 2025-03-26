package ru.fomin.auth.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fomin.auth.exception.OrderLineNotFoundException;
import ru.fomin.auth.mapper.OrderLineMapper;
import ru.fomin.auth.model.OrderLineRequest;
import ru.fomin.auth.model.OrderLineResponse;
import ru.fomin.auth.persistance.entity.OrderLine;
import ru.fomin.auth.persistance.repository.OrderLineRepository;
import ru.fomin.auth.util.OrderLineUtil;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class OrderLineServiceImplTest {

    @InjectMocks
    private OrderLineServiceImpl orderLineService;

    @Mock
    private OrderLineMapper orderLineMapper;

    @Mock
    private OrderLineRepository orderLineRepository;

    @Test
    @DisplayName("Test create orderLine functionality")
    public void givenOrderLineToSave_whenSave_thenOrderLineIsSaved(){
        //given
        OrderLineRequest orderLineRequest = OrderLineUtil.getOrderLineRequest1();
        Mockito.when(orderLineMapper.map(any(OrderLineRequest.class))).thenReturn(OrderLineUtil.getOrderLine1());
        Mockito.when(orderLineRepository.save(any(OrderLine.class))).thenReturn(OrderLineUtil.getOrderLine1().setId(1L));
        Mockito.when(orderLineMapper.map(any(OrderLine.class))).thenReturn(OrderLineUtil.getOrderLineResponse1());
        //when
        OrderLineResponse response = orderLineService.create(orderLineRequest);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getOrder());
        Assertions.assertNotNull(response.getGoods());
        Assertions.assertNotNull(response.getCount());
        Mockito.verify(orderLineMapper, Mockito.times(1)).map(any(OrderLineRequest.class));
        Mockito.verify(orderLineRepository, Mockito.times(1)).save(any(OrderLine.class));
        Mockito.verify(orderLineMapper,Mockito.times(1)).map(any(OrderLine.class));
    }

    @Test
    @DisplayName("Test update orderLine functionality")
    public void givenOrderLineToUpdate_whenUpdate_thenOrderLineIsUpdated(){
        //given
        OrderLineRequest orderLineRequest = OrderLineUtil.getOrderLineRequest1().setId(1L);
        Mockito.lenient().when(orderLineMapper.map(any(OrderLineRequest.class))).thenReturn(OrderLineUtil.getOrderLine1().setId(1L));
        Mockito.lenient().when(orderLineRepository.save(any(OrderLine.class))).thenReturn(OrderLineUtil.getOrderLine1().setId(1L));
        Mockito.lenient().when(orderLineMapper.map(any(OrderLine.class))).thenReturn(OrderLineUtil.getOrderLineResponse1());
        Mockito.when(orderLineRepository.existsById(anyLong())).thenReturn(true);
        //when
        OrderLineResponse response = orderLineService.update(orderLineRequest);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getOrder());
        Assertions.assertNotNull(response.getGoods());
        Assertions.assertNotNull(response.getCount());
        Mockito.verify(orderLineMapper,Mockito.times(1)).map(any(OrderLineRequest.class));
        Mockito.verify(orderLineRepository,Mockito.times(1)).save(any(OrderLine.class));
        Mockito.verify(orderLineMapper,Mockito.times(1)).map(any(OrderLine.class));
        Mockito.verify(orderLineRepository,Mockito.times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("Test find by id functionality")
    public void givenSavedOrderLine_whenFindById_thenOrderLineIsFound(){
        //given
        OrderLine orderLine = OrderLineUtil.getOrderLine1().setId(1L);
        Mockito.lenient().when(orderLineMapper.map(any(OrderLine.class))).thenReturn(OrderLineUtil.getOrderLineResponse1());
        Mockito.when(orderLineRepository.findById(anyLong())).thenReturn(Optional.of(OrderLineUtil.getOrderLine1().setId(1L)));
        //when
        OrderLineResponse response = orderLineService.findById(orderLine.getId());
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getOrder());
        Assertions.assertNotNull(response.getGoods());
        Assertions.assertNotNull(response.getCount());
        Mockito.verify(orderLineMapper,Mockito.times(1)).map(any(OrderLine.class));
        Mockito.verify(orderLineRepository,Mockito.times(1)).findById(anyLong());

    }

    @Test
    @DisplayName("Test find by id functionality when orderLine not found")
    public void givenSavedOrderLine_whenFindById_thenOrderLineIsNotFound(){
        //given
        OrderLine orderLine = OrderLineUtil.getOrderLine1().setId(1L);
        Mockito.lenient().when(orderLineMapper.map(any(OrderLine.class))).thenReturn(OrderLineUtil.getOrderLineResponse1());
        Mockito.when(orderLineRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        Assertions.assertThrows(OrderLineNotFoundException.class, () -> orderLineService.findById(orderLine.getId()));
        //then
        Mockito.verify(orderLineMapper,Mockito.never()).map(any(OrderLine.class));
        Mockito.verify(orderLineRepository,Mockito.times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Test find all functionality")
    public void givenTwoOrderLine_whenFindAll_thenOrderLineIsReturned(){
        //given
        OrderLine orderLine1 = OrderLineUtil.getOrderLine1().setId(1L);
        OrderLine orderLine2 = OrderLineUtil.getOrderLine2().setId(2L);
        List<OrderLine> orderLines = List.of(orderLine1,orderLine2);
        OrderLineResponse orderLineResponse1 = OrderLineUtil.getOrderLineResponse1();
        OrderLineResponse orderLineResponse2 = OrderLineUtil.getOrderLineResponse2();
        List<OrderLineResponse> orderLineResponses = List.of(orderLineResponse1,orderLineResponse2);
        Mockito.when(orderLineRepository.findAll()).thenReturn(orderLines);
        Mockito.when(orderLineMapper.map(orderLines)).thenReturn(orderLineResponses);
        //when
        List<OrderLineResponse> responses = orderLineService.findAll();
        //then
        Assertions.assertNotNull(responses);
        Assertions.assertEquals(responses.size(),2);
    }

    @Test
    @DisplayName("Test delete orderLine functionality")
    public void givenOrderLineToDelete_whenDelete_thenOrderLineIsDeleted(){
        //given
        OrderLine orderLine = OrderLineUtil.getOrderLine1().setId(1L);
        Mockito.when(orderLineRepository.findById(anyLong())).thenReturn(Optional.of(OrderLineUtil.getOrderLine1().setId(1L)));
        Mockito.doNothing().when(orderLineRepository).deleteById(anyLong());
        //when
        orderLineService.deleteById(orderLine.getId());
        //then
        Mockito.verify(orderLineRepository,Mockito.times(1)).findById(anyLong());
        Mockito.verify(orderLineRepository,Mockito.times(1)).deleteById(anyLong());

    }

    @Test
    @DisplayName("Test delete orderLine functionality when orderLine not found")
    public void givenOrderLineToDelete_whenDelete_thenOrderLineIsNotDeleted(){
        //given
        OrderLine orderLine = OrderLineUtil.getOrderLine1().setId(1L);
        Mockito.when(orderLineRepository.findById(anyLong())).thenReturn(Optional.empty());
        Mockito.lenient().doNothing().when(orderLineRepository).deleteById(anyLong());
        //when
        Assertions.assertThrows(OrderLineNotFoundException.class,()->orderLineService.deleteById(orderLine.getId()));
        //then
        Mockito.verify(orderLineRepository,Mockito.times(1)).findById(anyLong());
        Mockito.verify(orderLineRepository,Mockito.never()).deleteById(anyLong());

    }

}
