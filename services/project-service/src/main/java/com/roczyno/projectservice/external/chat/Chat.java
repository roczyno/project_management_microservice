package com.roczyno.projectservice.external.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
	private Integer id;
	private Integer projectId;
	private Set<Integer> teamMemberIds;
	private LocalDateTime createdAt;
}
