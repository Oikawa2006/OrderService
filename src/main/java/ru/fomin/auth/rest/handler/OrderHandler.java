package ru.fomin.auth.rest.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.fomin.auth.exception.OrderNotFoundException;
import ru.fomin.auth.exception.OrderRestException;

@RestControllerAdvice
public class OrderHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> orderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity.badRequest()
                .body(OrderRestException.builder()
                        .status(404)
                        .message(e.getMessage())
                        .build());
    }

}
