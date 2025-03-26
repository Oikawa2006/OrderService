package ru.fomin.auth.security.exception;

public class ExistWithThisEmailException extends RuntimeException {
    public ExistWithThisEmailException(String message) {
        super(message);
    }
}
