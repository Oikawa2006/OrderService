package ru.fomin.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "client")
    private String client;

    @JsonProperty(value = "date")
    private LocalDate date;

    @JsonProperty(value = "address")
    private String address;

}
