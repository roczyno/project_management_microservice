package com.roczyno.invitationservice.kafka;

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
public class InvitationProducer {
	private final KafkaTemplate<String, InvitationConfirmation> kafkaTemplate;

	public void sendInviteConfirmation(InvitationConfirmation invitationConfirmation){
		log.info("Sending Invite confirmation {}", invitationConfirmation);
		Message<InvitationConfirmation> message= MessageBuilder
				.withPayload(invitationConfirmation)
				.setHeader(KafkaHeaders.TOPIC,"invite-topic")
				.build();
		kafkaTemplate.send(message);
	}


}
