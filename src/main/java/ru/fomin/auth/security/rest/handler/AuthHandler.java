package ru.fomin.auth.security.rest.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.fomin.auth.security.exception.ExistWithThisEmailException;
import ru.fomin.auth.security.rest.model.UserErrorResponse;

@RestControllerAdvice
public class AuthHandler {

    @ExceptionHandler(ExistWithThisEmailException.class)
    public ResponseEntity<?> handleExistWithThisEmailException(ExistWithThisEmailException e) {
        return ResponseEntity
                .badRequest()
                .body(UserErrorResponse.builder()
                        .status(404)
                        .message("User with this email address is exist")
                        .build());
    }

}
