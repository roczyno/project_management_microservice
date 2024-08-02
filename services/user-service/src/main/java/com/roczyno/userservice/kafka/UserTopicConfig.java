package com.roczyno.userservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class UserTopicConfig {

	@Bean
	public NewTopic userTopic(){
		return TopicBuilder.name("user-topic").build();
	}
}
