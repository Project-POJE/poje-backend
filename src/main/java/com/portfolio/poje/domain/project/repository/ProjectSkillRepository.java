package com.portfolio.poje.domain.project.repository;

import com.portfolio.poje.domain.project.entity.ProjectSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, Long> {

    @Query(value = "select distinct ps.type from ProjectSkill ps where ps.project.id = :id")
    List<String> findDistinctTypeById(@Param(value = "id") Long id);

    List<ProjectSkill> findByProjectIdAndType(Long id, String type);

}
