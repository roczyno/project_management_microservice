package com.roczyno.notificationservice.model;

import com.roczyno.notificationservice.kafka.InvitationConfirmation;
import com.roczyno.notificationservice.kafka.IssueConfirmation;
import com.roczyno.notificationservice.kafka.UserConfirmation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Notification {
	@Id
	private String id;
	private NotificationType notificationType;
	private LocalDateTime notificationDate;
	private UserConfirmation userConfirmation;
	private InvitationConfirmation invitationConfirmation;
	private IssueConfirmation issueConfirmation;


}
