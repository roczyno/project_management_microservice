package com.roczyno.issueservice.repository;

import com.roczyno.issueservice.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue,Integer> {
	List<Issue> findByProjectId(Integer projectId);
}
