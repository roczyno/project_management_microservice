package com.roczyno.issueservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueProducer {
	private final KafkaTemplate<String,IssueConfirmation> kafkaTemplate;
	public void sendIssueConfirmation(IssueConfirmation issueConfirmation){
		log.info("sending issue confirmation");
		Message<IssueConfirmation> message= MessageBuilder
				.withPayload(issueConfirmation)
				.setHeader(KafkaHeaders.TOPIC,"issue-topic")
				.build();
		kafkaTemplate.send(message);
	}
}
