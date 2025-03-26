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
import ru.fomin.auth.exception.GoodsRestException;
import ru.fomin.auth.exception.OrderLineRestException;
import ru.fomin.auth.model.GoodsRequest;
import ru.fomin.auth.model.GoodsResponse;
import ru.fomin.auth.service.GoodsService;

import java.util.List;

@Tag(name = "goods")
@RestController
@RequestMapping("/api/v1/goods")
@RequiredArgsConstructor
public class GoodsRestController {

    private final GoodsService goodsService;

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    @Operation(
            summary = "Метод создания goods", operationId = "createGoods",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное создание goods",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoodsResponse.class)
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания goods",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GoodsRequest.class),
                            examples = @ExampleObject(
                                    name = "пример создания goods",
                                    summary = "пример запроса",
                                    description = "пример Json для goods",
                                    value = """
                                            {
                                                    "name": "Table",
                                                    "price": 10000
                                                }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<?> createGoods(@RequestBody GoodsRequest goodsRequest) {
        GoodsResponse goodsResponse = goodsService.saveGoods(goodsRequest);
        return ResponseEntity.ok(goodsResponse);
    }

    @Operation(
            summary = "Метод обновления goods", operationId = "updateGoods",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное обновление goods",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoodsResponse.class)
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
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания goods",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GoodsRequest.class),
                            examples = @ExampleObject(
                                    name = "пример создания goods",
                                    summary = "пример запроса",
                                    description = "Пример Json для goods",
                                    value = """
                                            {
                                                    "name": "Table",
                                                    "price": 10000
                                                }
                                            """
                            )
                    )
            )
    )
    @PutMapping
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<?> updateGoods(@RequestBody GoodsRequest goodsRequest) {
        GoodsResponse goodsResponse = goodsService.updateGoods(goodsRequest);
        return ResponseEntity.ok(goodsResponse);
    }

    @Operation(
            summary = "Метод получения goods по id", operationId = "findGoodsById",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение goods по id",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoodsResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Goods not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoodsRestException.class)
                            )
                    )
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "id goods",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string", example = "1")
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        GoodsResponse goodsResponse = goodsService.findGoods(id);
        return ResponseEntity.ok(goodsResponse);
    }

    @Operation(
            summary = "Метод получения всех goods", operationId = "findAllGoods",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение goods",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoodsResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Goods not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoodsRestException.class)
                            )
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> findAll() {
        List<GoodsResponse> goodsResponse = goodsService.findAllGoods();
        return ResponseEntity.ok(goodsResponse);
    }

    @Operation(
            summary = "Метод удаления goods", operationId = "deleteGoods",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление goods",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Goods not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GoodsRestException.class)
                            )
                    )
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "goods id",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string", example = "1")
                    )
            }
    )

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        goodsService.delete(id);
        return ResponseEntity.ok().build();
    }
    
}
