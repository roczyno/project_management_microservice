package com.roczyno.gatewayservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GlobalFiltersConfig {
	final Logger logger = LoggerFactory.getLogger(GlobalFiltersConfig.class);
	@Bean
	public GlobalFilter secondPrefilter(){
		return (exchange,chain)->{
			logger.info("My second global  pre-filter is executed...");
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				logger.info("My second global  post-filter is executed...");
			}));
		};
	}
}
