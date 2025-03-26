package ru.fomin.auth.persistance.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client", nullable = false)
    private String client;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "address", nullable = false)
    private String address;

    @Builder.Default
    @OneToMany(mappedBy = "order")
    private List<OrderLine> orderLines = new ArrayList<>();

}
