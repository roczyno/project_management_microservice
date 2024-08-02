package com.roczyno.userservice.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserProducer {
	private final KafkaTemplate<String,UserConfirmation> kafkaTemplate;
	public void sendUserConfirmation(UserConfirmation userConfirmation){
		log.info("sending user confirmation");
		Message<UserConfirmation> message= MessageBuilder
				.withPayload(userConfirmation)
				.setHeader(KafkaHeaders.TOPIC,"user-topic")
				.build();
		kafkaTemplate.send(message);
	}
}
