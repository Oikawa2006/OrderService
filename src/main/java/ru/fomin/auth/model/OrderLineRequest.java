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
@NoArgsConstructor
@AllArgsConstructor
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
