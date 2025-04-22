package com.roczyno.gatewayservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;


@Component
public class PreFilter implements GlobalFilter {
	final Logger logger = LoggerFactory.getLogger(PostFilter.class);
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		logger.info("this is the filter...");
		String requestPath =exchange.getRequest().getPath().toString();
		logger.info("Request path {}",requestPath);
		HttpHeaders httpHeaders=exchange.getRequest().getHeaders();
		Set<String> headerNames= httpHeaders.keySet();
		headerNames.forEach((headerName)->{
			String headerValue=httpHeaders.getFirst(headerName);
			logger.info("{} {}", headerNames, headerValue);
		});
		return chain.filter(exchange);
	}
}
