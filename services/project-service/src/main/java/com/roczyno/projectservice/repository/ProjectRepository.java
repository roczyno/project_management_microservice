package com.roczyno.projectservice.repository;

import com.roczyno.projectservice.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Integer> {
	@Query("select p from Project p where p.userId = :userId or :teamUserId member of p.teamMemberIds")
	List<Project> findByTeamOrOwner(@Param("userId") Integer userId, @Param("teamUserId") Integer teamUserId);

	@Query("select p.teamMemberIds from Project p where p.id = :projectId")
	List<Integer> findTeamMemberIdsByProjectId(@Param("projectId") Integer projectId);

	List<Project> findByNameContainingIgnoreCase(String s);
}
