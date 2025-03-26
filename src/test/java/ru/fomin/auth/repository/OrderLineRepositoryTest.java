package ru.fomin.auth.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.fomin.auth.exception.OrderLineNotFoundException;
import ru.fomin.auth.persistance.entity.OrderLine;
import ru.fomin.auth.persistance.repository.GoodsRepository;
import ru.fomin.auth.persistance.repository.OrderLineRepository;
import ru.fomin.auth.persistance.repository.OrderRepository;
import ru.fomin.auth.util.OrderLineUtil;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Transactional
public class OrderLineRepositoryTest {

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GoodsRepository goodsRepository;

    @BeforeEach
    public void setUp() {
        orderLineRepository.deleteAll();
        orderRepository.deleteAll();
        goodsRepository.deleteAll();
    }

    @Test
    @DisplayName("Test create orderLine functionality")
    public void givenOrderLineToSave_whenSave_thenIsSaved() throws Exception {
        //given
        OrderLine orderLineToSave = OrderLineUtil.getOrderLine1();
        //when
        OrderLine savedOrderLine = orderLineRepository.save(orderLineToSave);
        //then
        Assertions.assertNotNull(savedOrderLine.getId());
        Assertions.assertNotNull(savedOrderLine.getOrder());
        Assertions.assertNotNull(savedOrderLine.getGoods());
        Assertions.assertNotNull(savedOrderLine.getCount());
    }

    @Test
    @DisplayName("Test update orderLine functionality")
    public void givenOrderLineToUpdate_whenUpdate_thenIsUpdated() throws Exception {
        //given
        long updatedCount = 5L;
        OrderLine orderLineToSave = OrderLineUtil.getOrderLine1();
        orderLineRepository.save(orderLineToSave);
        //when
        OrderLine orderLineToUpdate = orderLineRepository.findById(orderLineToSave.getId())
                .orElseThrow(() -> new OrderLineNotFoundException("OrderLine not found"));
        orderLineToUpdate.setCount(updatedCount);
        OrderLine updatedOrderLine = orderLineRepository.save(orderLineToUpdate);
        //then
        Assertions.assertNotNull(updatedOrderLine.getId());
        Assertions.assertNotNull(updatedOrderLine.getOrder());
        Assertions.assertNotNull(updatedOrderLine.getGoods());
        Assertions.assertNotNull(updatedOrderLine.getCount());
        Assertions.assertEquals(updatedCount, updatedOrderLine.getCount());
    }

    @Test
    @DisplayName("Test find by id functionality")
    public void givenSaverOrderLine_whenFindById_thenIsFound() throws Exception {
        //given
        OrderLine orderLineToSave = OrderLineUtil.getOrderLine1();
        orderLineRepository.save(orderLineToSave);
        //when
        OrderLine orderLineToFound = orderLineRepository.findById(orderLineToSave.getId())
                .orElseThrow(() -> new OrderLineNotFoundException("OrderLine not found"));
        //then
        Assertions.assertNotNull(orderLineToFound.getId());
        Assertions.assertNotNull(orderLineToFound.getOrder());
        Assertions.assertNotNull(orderLineToFound.getGoods());
        Assertions.assertNotNull(orderLineToFound.getCount());
    }

    @Test
    @DisplayName("Test find by id when orderLine not found")
    public void givenNotSavedOrderLine_whenFindById_thenIsNotFound() throws Exception {
        //given
        // When
        Optional<OrderLine> result = orderLineRepository.findById(-1L);
        // Then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test find all functionality")
    public void givenTwoOrderLine_whenFindAll_thenOrderLinesIsReturned() throws Exception {
        //given
        OrderLine orderLineToSave1 = OrderLineUtil.getOrderLine1();
        OrderLine orderLineToSave2 = OrderLineUtil.getOrderLine2();
        orderLineRepository.saveAll(List.of(orderLineToSave1,orderLineToSave2));
        //when
        List<OrderLine> orderLines = orderLineRepository.findAll();
        //then
        Assertions.assertNotNull(orderLines);
        Assertions.assertEquals(orderLines.size(), 2);
    }

    @Test
    @DisplayName("Test delete orderLine functionality")
    public void givenSavedOrderLine_whenDelete_thenIsDeleted() throws Exception {
        //given
        OrderLine orderLineToSave = OrderLineUtil.getOrderLine1();
        orderLineRepository.save(orderLineToSave);
        //when
        orderLineRepository.deleteById(orderLineToSave.getId());
        //then
        Optional<OrderLine> result = orderLineRepository.findById(-1L);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test delete orderLine functionality when orderLine not found")
    public void givenNotSavedOrderLine_whenDelete_thenIsNotFound() throws Exception {
        //given
        //when
        orderLineRepository.deleteById(-1L);
        //then
        Optional<OrderLine> result = orderLineRepository.findById(-1L);
        Assertions.assertTrue(result.isEmpty());

    }

}

