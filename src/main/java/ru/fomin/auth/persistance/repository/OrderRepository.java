package ru.fomin.auth.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fomin.auth.persistance.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
