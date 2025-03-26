package ru.fomin.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrderLineRequest {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "order")
    private OrderRequest order;

    @JsonProperty(value = "goods")
    private GoodsRequest goods;

    @JsonProperty(value = "count")
    private long count;


}
