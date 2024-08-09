package com.roczyno.notificationservice.kafka;

import com.roczyno.notificationservice.email.EmailService;
import com.roczyno.notificationservice.email.EmailTemplate;
import com.roczyno.notificationservice.model.Notification;
import com.roczyno.notificationservice.model.NotificationType;
import com.roczyno.notificationservice.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
	private final NotificationRepository notificationRepository;
	private final EmailService emailService;

	@KafkaListener(topics = "user-topic")
	public void consumeUserConfirmationSuccess(UserConfirmation userConfirmation){
		log.info(format("Consuming the message from user-topic Topic:: %s",userConfirmation));
		notificationRepository.save(
				Notification.builder()
						.notificationDate(LocalDateTime.now())
						.userConfirmation(userConfirmation)
						.notificationType(NotificationType.USER_CONFIRMATION)
						.build()
		);
		try {
			emailService.sendEmail(
					userConfirmation.to(),
					userConfirmation.username(),
					EmailTemplate.ACTIVATE_ACCOUNT,
					userConfirmation.confirmationUrl(),
					userConfirmation.activationCode(),
					userConfirmation.subject()
			);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	@KafkaListener(topics = "invite-topic")
	public void consumeInviteConfirmationSuccess(InvitationConfirmation invitationConfirmation){
		log.info(format("Consuming the message from invite-topic Topic:: %s", invitationConfirmation));
		notificationRepository.save(
				Notification.builder()
						.notificationDate(LocalDateTime.now())
						.invitationConfirmation(invitationConfirmation)
						.notificationType(NotificationType.INVITE_CONFIRMATION)
						.build()
		);
		try {
			emailService.sendInviteEmail(
					invitationConfirmation.to(),
					invitationConfirmation.senderName(),
					EmailTemplate.SEND_INVITE,
					invitationConfirmation.subject(),
					invitationConfirmation.projectName(),
					invitationConfirmation.InvitationUrl()
			);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	@KafkaListener(topics = "issue-topic")
	public void consumeIssueConfirmationSuccess(IssueConfirmation issueConfirmation){
		log.info(format("Consuming the message from issue-topic Topic:: %s",issueConfirmation));
		notificationRepository.save(
				Notification.builder()
						.notificationDate(LocalDateTime.now())
						.issueConfirmation(issueConfirmation)
						.notificationType(NotificationType.ISSUE_CONFIRMATION)
						.build()
		);
		try {
			emailService.sendAssignIssue(
					issueConfirmation.userEmail(),
					issueConfirmation.senderName(),
					EmailTemplate.SEND_ISSUE,
					issueConfirmation.subject(),
					issueConfirmation.projectName(),
					issueConfirmation.priority(),
					issueConfirmation.dueDate(),
					issueConfirmation.status(),
					issueConfirmation.title(),
					issueConfirmation.description()
			);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

}
