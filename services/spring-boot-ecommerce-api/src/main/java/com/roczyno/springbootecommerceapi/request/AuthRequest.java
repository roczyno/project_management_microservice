package com.roczyno.springbootecommerceapi.request;

public record AuthRequest(
        String email,
        String password
) {
}
