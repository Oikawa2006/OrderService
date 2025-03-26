package ru.fomin.auth.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.fomin.auth.exception.OrderLineRestException;
import ru.fomin.auth.model.OrderLineRequest;
import ru.fomin.auth.model.OrderLineResponse;
import ru.fomin.auth.service.OrderLineService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orderLine")
@Tag(name = "orderLine")
public class OrderLineRestController {

    private final OrderLineService orderLineService;

    @Operation(
            summary = "Метод добавления orderLine", operationId = "createOrderLine",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "orderLine успешно создан",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderLineResponse.class)
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "данные для создания oderLine",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderLineRequest.class),
                            examples = @ExampleObject(
                                    name = "Пример создания orderLine",
                                    summary = "Пример запроса",
                                    description = "Пример Json для orderLine",
                                    value = """
                                            {
                                                "order": {
                                                    "client": "Danil",
                                                    "date": "2025-03-13",
                                                    "address": "Space"
                                                },
                                                "goods": {
                                                    "name": "Table",
                                                    "price": 10000
                                                },
                                                "count": 5
                                            }
                                            """
                            )
                    )

            )

    )
    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<?> create(@RequestBody OrderLineRequest orderLineRequest) {
        OrderLineResponse orderLineResponse = orderLineService.create(orderLineRequest);
        return ResponseEntity.ok(orderLineResponse);
    }

    @Operation(summary = "Метод обновления orderLine", operationId = "updateOrderLine",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "orderLine успешно обновлен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderLineResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "orderLine not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderLineRestException.class)
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "данные для создания oderLine",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderLineRequest.class),
                            examples = @ExampleObject(
                                    name = "Пример создания orderLine",
                                    summary = "Пример запроса",
                                    description = "Пример Json для orderLine",
                                    value = """
                                            {
                                                "order": {
                                                    "client": "Danil",
                                                    "date": "2025-03-13",
                                                    "address": "Space"
                                                },
                                                "goods": {
                                                    "name": "Table",
                                                    "price": 10000
                                                },
                                                "count": 5
                                            }
                                            """
                            )
                    )
            )


    )
    @PutMapping
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<?> update(@RequestBody OrderLineRequest orderLineRequest) {
        OrderLineResponse orderLineResponse = orderLineService.update(orderLineRequest);
        return ResponseEntity.ok(orderLineResponse);
    }

    @Operation(
            summary = "Метод получения orderLine по id", description = "findOrderLineById",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение orderLine",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderLineResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "OrderLine not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderLineRestException.class)
                            )
                    )
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "id orderLine",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string", example = "1")
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> getOrderLineById(@PathVariable Long id) {
        OrderLineResponse orderLineResponse = orderLineService.findById(id);
        return ResponseEntity.ok(orderLineResponse);
    }

    @Operation(
            summary = "Метод получения всех orderLine", description = "findAllOrderLine",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение всех orderLine",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderLineResponse.class)
                            )
                    )
            }

    )
    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> getAllOrderLines() {
        List<OrderLineResponse> orderLineResponses = orderLineService.findAll();
        return ResponseEntity.ok(orderLineResponses);
    }

    @Operation(
            summary = "Метод удаления orderLine", operationId = "deleteOrderLine",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление orderLine",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "OrderLine not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderLineRestException.class)
                            )
                    )
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "id orderLine",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string", example = "1")
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<?> deleteOrderLineById(@PathVariable Long id) {
        orderLineService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
