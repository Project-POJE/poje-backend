package com.portfolio.poje.service.project;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillCreateReq;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillDeleteReq;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillInfoReq;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillListReq;
import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.domain.project.ProjectSkill;
import com.portfolio.poje.repository.project.ProjectRepository;
import com.portfolio.poje.repository.project.ProjectSkillRepository;
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
     * @param prSkillCreateReq
     */
    @Transactional
    public void enroll(PrSkillCreateReq prSkillCreateReq){
        Project project = projectRepository.findById(prSkillCreateReq.getProjectId()).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        for (PrSkillListReq skillSet : prSkillCreateReq.getSkillSet()){
            for (PrSkillInfoReq skillInfo : skillSet.getSkills()){
                ProjectSkill projectSkill = ProjectSkill.builder()
                        .type(skillSet.getType())
                        .name(skillInfo.getName())
                        .project(project)
                        .build();

                projectSkillRepository.save(projectSkill);
            }
        }

    }


    /**
     * 프로젝트 사용 기술 삭제
     * @param prSkillDeleteReq
     */
    @Transactional
    public void deleteProjectSkill(PrSkillDeleteReq prSkillDeleteReq){
        Project project = projectRepository.findById(prSkillDeleteReq.getProjectId()).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        ProjectSkill projectSkill = projectSkillRepository.findById(prSkillDeleteReq.getSkillId()).orElseThrow(
                () -> new PojeException(ErrorCode.SKILL_NOT_FOUND)
        );

        project.getProjectSkills().remove(projectSkill);
        projectSkillRepository.delete(projectSkill);
    }

}
