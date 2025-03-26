package ru.fomin.auth.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fomin.auth.persistance.entity.OrderLine;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

}
