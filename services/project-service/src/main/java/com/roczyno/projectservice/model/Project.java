package com.roczyno.projectservice.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private String category;
	@ElementCollection
	@Builder.Default
	private List<String> tags = new ArrayList<>();
	private Integer userId;
	@ElementCollection
	@Builder.Default
	private List<Integer> teamMemberIds = new ArrayList<>();
	private LocalDateTime createdAt;

}
