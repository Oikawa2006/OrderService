package ru.fomin.auth.exception;

public class OrderLineNotFoundException extends RuntimeException {

    public OrderLineNotFoundException(String message) {
        super(message);
    }

}
