package com.portfolio.poje.repository.project;

import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.domain.project.ProjectImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectImgRepository extends JpaRepository<ProjectImg, Long> {
    List<ProjectImg> findByProject(Project project);
}
