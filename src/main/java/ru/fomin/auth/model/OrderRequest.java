package ru.fomin.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
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
