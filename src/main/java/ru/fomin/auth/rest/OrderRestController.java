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
import ru.fomin.auth.exception.OrderRestException;
import ru.fomin.auth.model.OrderRequest;
import ru.fomin.auth.model.OrderResponse;
import ru.fomin.auth.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Tag(name = "Order")
public class OrderRestController {

    private final OrderService orderService;

    @Operation(
            summary = "Метод добавления order", operationId = "createOrder",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "order успешно создан",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponse.class)
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "данные для создания order",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderRequest.class),
                            examples = @ExampleObject(
                                    name = "Пример создания order",
                                    summary = "Пример запроса",
                                    description = "Пример Json для order",
                                    value = """
                                            {
                                                    "client": "Danil",
                                                    "date": "2025-03-13",
                                                    "address": "Space"
                                            }
                                            """
                            )
                    )
            )
    )
    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.saveOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }

    @Operation(summary = "Метод обновления order", operationId = "updateOrder",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "order успешно обновлен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "order not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderRestException.class)
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "данные для создания order",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderRequest.class),
                            examples = @ExampleObject(
                                    name = "Пример создания order",
                                    summary = "Пример запроса",
                                    description = "Пример Json для order",
                                    value = """
                                            {
                                                    "client": "Danil",
                                                    "date": "2025-03-13",
                                                    "address": "Space"
                                            }
                                            """
                            )
                    )
            )


    )
    @PutMapping
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<?> updateOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.updateOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }

    @Operation(
            summary = "Метод получения order по id", description = "findOrderById",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение order",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "order not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderRestException.class)
                            )
                    )
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "id order",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string", example = "1")
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> findById(@PathVariable long id) {
        OrderResponse orderResponse = orderService.findById(id);
        return ResponseEntity.ok(orderResponse);
    }

    @Operation(
            summary = "Метод получения всех order", description = "findAllOrder",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение всех order",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponse.class)
                            )
                    )
            }

    )
    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> findALl() {
        List<OrderResponse> orderResponseList = orderService.findAll();
        return ResponseEntity.ok(orderResponseList);
    }

    @Operation(
            summary = "Метод удаления order", operationId = "deleteOrder",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление order",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "order not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderRestException.class)
                            )
                    )
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "id order",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string", example = "1")
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.ok().build();
    }

}
