package ru.fomin.auth.rest.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.fomin.auth.exception.GoodsNotFoundException;
import ru.fomin.auth.exception.GoodsRestError;

@RestControllerAdvice
public class GoodsHandler {
    @ExceptionHandler(GoodsNotFoundException.class)
    public ResponseEntity<?> goodsNotFound(GoodsNotFoundException e) {
        return ResponseEntity.badRequest()
                .body(GoodsRestError.builder()
                        .status(404)
                        .message(e.getMessage())
                        .build());
    }
}
