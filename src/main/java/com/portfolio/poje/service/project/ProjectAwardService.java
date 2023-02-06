package com.portfolio.poje.service.project;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.project.projectAwardDto.PrAwardCreateReq;
import com.portfolio.poje.controller.project.projectAwardDto.PrAwardUpdateReq;
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
     * @param prAwardCreateReq
     */
    @Transactional
    public void enroll(PrAwardCreateReq prAwardCreateReq){
        Project project = projectRepository.findById(prAwardCreateReq.getProjectId()).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        ProjectAward projectAward = ProjectAward.enrollProjectAward()
                .supervision(prAwardCreateReq.getSupervision())
                .grade(prAwardCreateReq.getGrade())
                .description(prAwardCreateReq.getDescription())
                .project(project)
                .build();

        projectAwardRepository.save(projectAward);
    }


    /**
     * 프로젝트 수상 정보 수정
     * @param prAwardUpdateReq
     */
    @Transactional
    public void updateAwardInfo(Long projectId, PrAwardUpdateReq prAwardUpdateReq){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        ProjectAward projectAward;
        if (project.getProjectAward() == null){
            projectAward = ProjectAward.enrollProjectAward()
                    .project(project)
                    .build();
        } else {
            projectAward = project.getProjectAward();
        }

        projectAward.updateInfo(prAwardUpdateReq.getSupervision(),
                                prAwardUpdateReq.getGrade(),
                                prAwardUpdateReq.getDescription());
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
