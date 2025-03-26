package ru.fomin.auth.rest.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.fomin.auth.exception.OrderLineNotFoundException;
import ru.fomin.auth.exception.OrderLineRestError;

@RestControllerAdvice
public class OrderLineRestHandler {
    @ExceptionHandler
    public ResponseEntity<?> handleException(OrderLineNotFoundException e) {
        return ResponseEntity.badRequest()
                .body(OrderLineRestError.builder()
                        .message(e.getMessage())
                        .status(404)
                        .build());
    }
}
