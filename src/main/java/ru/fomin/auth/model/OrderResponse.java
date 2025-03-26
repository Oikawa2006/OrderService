package ru.fomin.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "client")
    private String client;

    @JsonProperty(value = "date")
    private LocalDate date;

    @JsonProperty(value = "address")
    private String address;
}
