package com.roczyno.springbootecommerceapi.request;

public record changePasswordRequest(
        String oldPassword,
        String newPassword,
        String confirmPassword
) {
}
