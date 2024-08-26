package com.roczyno.springbootecommerceapi.request;

public record PasswordUpdateRequest(
        String password,
        String repeatPassword
) {
}
