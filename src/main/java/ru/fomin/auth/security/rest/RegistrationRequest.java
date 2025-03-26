package ru.fomin.auth.security.rest;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String email;
    private String username;
    private String password;
}
