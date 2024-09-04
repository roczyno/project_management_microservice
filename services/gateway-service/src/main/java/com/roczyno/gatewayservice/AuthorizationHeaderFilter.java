package com.roczyno.gatewayservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

	// Ideally, load this from configuration or environment variables
	private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
	private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));

	public AuthorizationHeaderFilter() {
		super(Config.class);
	}

	public static class Config {
	}

	@Override
	public GatewayFilter apply(Config config) {

		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			// Check for Authorization header
			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
			}

			String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = authHeader.replace("Bearer", "").trim();

			// Validate JWT token
			if (!isJwtValid(jwt)) {
				return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
			}

			// Continue filter chain
			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		// You can also log the error or return a response body if needed
		return response.setComplete();
	}

	private boolean isJwtValid(String jwt) {
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(SIGNING_KEY)
					.build()
					.parseClaimsJws(jwt)
					.getBody();

			String subject = claims.getSubject();

			return subject != null && !subject.isEmpty();

		} catch (JwtException | IllegalArgumentException e) {
			// Handle specific JWT exceptions if needed
			return false;
		}
	}
}
