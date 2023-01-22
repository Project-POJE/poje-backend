package com.portfolio.poje.service;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.project.projectAwardDto.ProjectSkillCreateRequestDto;
import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.domain.project.ProjectSkill;
import com.portfolio.poje.repository.ProjectRepository;
import com.portfolio.poje.repository.ProjectSkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectSkillService {

    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;


    /**
     * 프로젝트 사용 기술 추가
     * @param projectSkillCreateRequestDto
     * @param projectId
     */
    @Transactional
    public void enroll(ProjectSkillCreateRequestDto projectSkillCreateRequestDto, Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        ProjectSkill projectSkill = ProjectSkill.builder()
                .skill(projectSkillCreateRequestDto.getSkill())
                .project(project)
                .build();

        projectSkillRepository.save(projectSkill);
    }


    /**
     * 프로젝트 사용 기술 삭제
     * @param projectId
     * @param skillId
     */
    @Transactional
    public void deleteProjectSkill(Long projectId, Long skillId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        ProjectSkill projectSkill = projectSkillRepository.findById(skillId).orElseThrow(
                () -> new PojeException(ErrorCode.SKILL_NOT_FOUND)
        );

        project.getProjectSkills().remove(projectSkill);
        projectSkillRepository.delete(projectSkill);
    }

}
