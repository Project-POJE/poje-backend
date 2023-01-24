package com.portfolio.poje.repository.project;

import com.portfolio.poje.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
