package com.roczyno.invitationservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaInviteTopicConfig {
	@Bean
	public NewTopic inviteTopic(){
		return TopicBuilder
				.name("invite-topic")
				.build();
	}
}
