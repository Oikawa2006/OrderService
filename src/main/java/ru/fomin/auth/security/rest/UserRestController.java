package ru.fomin.auth.security.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.fomin.auth.security.rest.model.UserErrorResponse;
import ru.fomin.auth.security.rest.model.UserRequest;
import ru.fomin.auth.security.rest.model.UserResponse;
import ru.fomin.auth.security.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Метод добавления user", operationId = "createUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "user успешно создан",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "данные для создания user",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequest.class),
                            examples = @ExampleObject(
                                    name = "Пример создания user",
                                    summary = "Пример запроса",
                                    description = "Пример Json для user",
                                    value = """
                                            {
                                                "email": "admin@mail.com",
                                                "password": "admin"
                                            }
                                            """
                            )
                    )
            )
    )
    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<?> createOrder(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.saveUser(userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Метод обновления user", operationId = "updateUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "user успешно обновлен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "user not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserErrorResponse.class)
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "данные для создания user",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequest.class),
                            examples = @ExampleObject(
                                    name = "Пример создания user",
                                    summary = "Пример запроса",
                                    description = "Пример Json для user",
                                    value = """
                                            {
                                                "email": "admin@mail.com",
                                                "password": "admin"
                                            }
                                            """
                            )
                    )
            )
    )
    @PutMapping
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<?> updateOrder(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.updateUser(userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Метод получения user по id", description = "findUserById",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "user not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserErrorResponse.class)
                            )
                    )
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "id user",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string", example = "1")
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        UserResponse userResponse = userService.findUserById(id);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Метод получения user по id", description = "findUserById",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "user not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserErrorResponse.class)
                            )
                    )
            },
            parameters = {
                    @Parameter(
                            name = "email",
                            description = "email user",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string", example = "admin@mail.com")
                    )
            }
    )
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> findByEmail(@PathVariable("email") String email) {
        UserResponse userResponse = userService.findUserByEmail(email);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Метод получения всех user", description = "findAllUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение всех user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    )
            }

    )
    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<?> findAll() {
        List<UserResponse> userResponses = userService.findAllUsers();
        return ResponseEntity.ok(userResponses);
    }

    @Operation(
            summary = "Метод удаления user", operationId = "deleteUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление user",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "user not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserErrorResponse.class)
                            )
                    )
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "id user",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string", example = "1")
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

}
