package ru.fomin.auth.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fomin.auth.persistance.entity.Goods;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
}
