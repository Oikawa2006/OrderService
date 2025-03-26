package ru.fomin.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderLineResponse {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "order")
    private OrderResponse order;

    @JsonProperty(value = "goods")
    private GoodsResponse goods;

    @JsonProperty(value = "count")
    private long count;

}
