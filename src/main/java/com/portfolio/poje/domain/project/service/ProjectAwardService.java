package com.portfolio.poje.domain.project.service;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.domain.project.dto.PrAwardDto;
import com.portfolio.poje.domain.project.entity.Project;
import com.portfolio.poje.domain.project.entity.ProjectAward;
import com.portfolio.poje.domain.project.repository.ProjectRepository;
import com.portfolio.poje.domain.project.repository.ProjectAwardRepository;
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
     * @Param projectId
     * @param prAwardCreateReq
     */
    @Transactional
    public void enroll(Long projectId, PrAwardDto.PrAwardCreateReq prAwardCreateReq){
        Project project = projectRepository.findById(projectId).orElseThrow(
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
    public void updateAwardInfo(Long projectId, PrAwardDto.PrAwardUpdateReq prAwardUpdateReq){
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
