package ru.fomin.auth.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.fomin.auth.persistance.entity.Order;
import ru.fomin.auth.persistance.repository.OrderRepository;
import ru.fomin.auth.util.DataUtilOrder;

import java.util.List;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp(){
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save order functionality")
    public void givenOrderToSave_whenSave_thenOrderIsSaved(){
        //given
        Order orderToSave = DataUtilOrder.getOrder1();
        //when
        Order savedOrder = orderRepository.save(orderToSave);
        //then
        Assertions.assertNotNull(savedOrder);
        Assertions.assertNotNull(savedOrder.getId());
        Assertions.assertNotNull(savedOrder.getClient());
        Assertions.assertNotNull(savedOrder.getDate());
        Assertions.assertNotNull(savedOrder.getAddress());
    }

    @Test
    @DisplayName("Test update order functionality")
    public void givenOrderToUpdate_whenUpdate_thenOrderIsUpdated(){
        //given
        String updatedClient="updatedClient";
        Order orderToSave = DataUtilOrder.getOrder1();
        orderRepository.save(orderToSave);
        orderToSave.setClient(updatedClient);
        //when
        Order orderToUpdate = orderRepository.findById(orderToSave.getId())
                .orElse(null);
        orderToUpdate.setClient(updatedClient);
        orderRepository.save(orderToUpdate);
        //then
        Assertions.assertNotNull(orderToUpdate);
        Assertions.assertNotNull(orderToUpdate.getId());
        Assertions.assertNotNull(orderToUpdate.getClient());
        Assertions.assertEquals(updatedClient, orderToUpdate.getClient());
        Assertions.assertNotNull(orderToUpdate.getDate());
        Assertions.assertNotNull(orderToUpdate.getAddress());
    }

    @Test
    @DisplayName("Test get by id order functionality")
    public void givenOrderToFindById_whenFindById_thenOrderIsFound(){
        //given
        Order orderToSave = DataUtilOrder.getOrder1();
        orderRepository.save(orderToSave);
        //when
        Order foundOrder = orderRepository.findById(orderToSave.getId())
                .orElse(null);
        //then
        Assertions.assertNotNull(foundOrder);
        Assertions.assertNotNull(foundOrder.getId());
        Assertions.assertNotNull(foundOrder.getClient());
        Assertions.assertNotNull(foundOrder.getDate());
        Assertions.assertNotNull(foundOrder.getAddress());
    }

    @Test
    @DisplayName("Test get by id order functionality when order not found")
    public void givenOrderToFindById_whenFindById_thenOrderIsNotFound(){
        //given
        //when
        Order foundOrder = orderRepository.findById(-1L)
                .orElse(null);
        //then
        Assertions.assertNull(foundOrder);
    }

    @Test
    @DisplayName("Test get all orders functionality")
    public void givenThreeOrders_whenFindAl_thenOrdersIsReturned(){
        //given
        Order order1 = DataUtilOrder.getOrder1();
        Order order2 = DataUtilOrder.getOrder2();
        Order order3 = DataUtilOrder.getOrder3();
        List<Order> orders = List.of(order1,order2,order3);
        orderRepository.saveAll(orders);
        //when
        List<Order> foundOrders = orderRepository.findAll();
        //then
        Assertions.assertNotNull(foundOrders);
        Assertions.assertEquals(3, foundOrders.size());
    }

    @Test
    @DisplayName("Test delete order by id functionality")
    public void givenOrderToDelete_whenDelete_thenOrderIsDeleted(){
        //given
        Order orderToSave = DataUtilOrder.getOrder1();
        orderRepository.save(orderToSave);
        //when
        orderRepository.deleteById(orderToSave.getId());
        //then
        Order orderToFound = orderRepository.findById(orderToSave.getId())
                .orElse(null);
        Assertions.assertNull(orderToFound);
    }

}
