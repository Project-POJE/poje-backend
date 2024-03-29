package com.portfolio.poje.domain.project.service.serviceImpl;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.domain.project.dto.PrAwardDto;
import com.portfolio.poje.domain.project.entity.Project;
import com.portfolio.poje.domain.project.entity.ProjectAward;
import com.portfolio.poje.domain.project.repository.ProjectRepository;
import com.portfolio.poje.domain.project.service.ProjectAwardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProjectAwardServiceImpl implements ProjectAwardService {

    private final ProjectRepository projectRepository;


    /**
     * 프로젝트 수상 정보 수정
     * @param projectId
     * @param prAwardUpdateReq
     */
    @Override
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

}
