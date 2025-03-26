package ru.fomin.auth.security.model;

import lombok.Getter;

@Getter
public enum Permission {

    WRITE("write"),
    READ("read");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

}
