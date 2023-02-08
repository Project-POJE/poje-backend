package com.portfolio.poje.domain.project.repository;

import com.portfolio.poje.domain.project.entity.Project;
import com.portfolio.poje.domain.project.entity.ProjectImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectImgRepository extends JpaRepository<ProjectImg, Long> {
    List<ProjectImg> findByProject(Project project);
}
