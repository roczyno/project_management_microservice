package com.roczyno.projectservice.external.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class Chat {
	private Integer id;
	private Integer projectId;
	private Set<Integer> teamMemberIds;
	private LocalDateTime createdAt;
}
