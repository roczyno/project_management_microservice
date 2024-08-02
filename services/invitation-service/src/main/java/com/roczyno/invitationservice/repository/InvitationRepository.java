package com.roczyno.invitationservice.repository;

import com.roczyno.invitationservice.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation,Integer> {
	Invitation findByToken(String token);
}
