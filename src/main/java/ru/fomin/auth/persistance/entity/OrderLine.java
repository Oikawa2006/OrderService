package ru.fomin.auth.persistance.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "orderline_table")
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @Column(name = "count")
    private long count;

}
