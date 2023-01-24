package com.portfolio.poje.service.project;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.project.projectAwardDto.ProjectAwardCreateRequestDto;
import com.portfolio.poje.controller.project.projectAwardDto.ProjectAwardUpdateRequestDto;
import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.domain.project.ProjectAward;
import com.portfolio.poje.repository.project.ProjectAwardRepository;
import com.portfolio.poje.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectAwardService {

    private final ProjectRepository projectRepository;
    private final ProjectAwardRepository projectAwardRepository;


    /**
     * 프로젝트 수상 정보 등록
     * @param projectAwardCreateRequestDto
     */
    @Transactional
    public void enroll(ProjectAwardCreateRequestDto projectAwardCreateRequestDto, Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        ProjectAward projectAward = ProjectAward.enrollProjectAward()
                .supervision(projectAwardCreateRequestDto.getSupervision())
                .grade(projectAwardCreateRequestDto.getGrade())
                .description(projectAwardCreateRequestDto.getDescription())
                .project(project)
                .build();

        projectAwardRepository.save(projectAward);
    }


    /**
     * 프로젝트 수상 정보 수정
     * @param projectAwardUpdateRequestDto
     * @param projectId
     */
    @Transactional
    public void updateAwardInfo(ProjectAwardUpdateRequestDto projectAwardUpdateRequestDto, Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        ProjectAward projectAward = project.getProjectAward();

        projectAward.updateInfo(projectAwardUpdateRequestDto.getSupervision(),
                                projectAwardUpdateRequestDto.getGrade(),
                                projectAwardUpdateRequestDto.getDescription());
    }


    /**
     * 프로젝트 수상 정보 삭제
     * @param projectId
     */
    @Transactional
    public void deleteAward(Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        project.insertAward(null);
        projectAwardRepository.delete(project.getProjectAward());
    }

}
