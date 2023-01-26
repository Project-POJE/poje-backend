package com.portfolio.poje.service.project;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.project.projectDto.ProjectBasicInfoResponse;
import com.portfolio.poje.controller.project.projectDto.ProjectUpdateRequest;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.repository.portfolio.PortfolioRepository;
import com.portfolio.poje.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectService {

    private final PortfolioRepository portfolioRepository;
    private final ProjectRepository projectRepository;


    /**
     * 기본 프로젝트 생성
     * @param portfolioId
     * @return
     */
    @Transactional
    public ProjectBasicInfoResponse enrollBasicProject(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        Project project = Project.createProject()
                .portfolio(portfolio)
                .build();

        projectRepository.save(project);

        return new ProjectBasicInfoResponse(project.getId());
    }


    @Transactional
    public void updateProject(ProjectUpdateRequest projectUpdateRequest, Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        project.updateInfo(projectUpdateRequest.getName(), projectUpdateRequest.getDuration(),
                            projectUpdateRequest.getDescription(), projectUpdateRequest.getBelong(),
                            projectUpdateRequest.getLink());

    }


    /**
     * 프로젝트 삭제
     * @param portfolioId
     * @param projectId
     */
    @Transactional
    public void deleteProject(Long portfolioId, Long projectId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        portfolio.getProjects().remove(project);
        projectRepository.delete(project);
    }

}
