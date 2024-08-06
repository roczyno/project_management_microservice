package com.roczyno.issueservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaIssueTopicConfig {
	@Bean
	public NewTopic issueTopic(){
		return TopicBuilder
				.name("issue-topic")
				.build();
	}
}
