package ru.fomin.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
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
