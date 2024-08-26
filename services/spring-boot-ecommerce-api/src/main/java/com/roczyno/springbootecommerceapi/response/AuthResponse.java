package com.roczyno.springbootecommerceapi.response;
import lombok.Builder;

@Builder
public record AuthResponse(
        String jwt,
		UserProfileResponse user,
       String message

) {


}
