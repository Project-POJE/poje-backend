package com.portfolio.poje.service;

import com.portfolio.poje.common.FileHandler;
import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.project.projectDto.ProjectCreateRequestDto;
import com.portfolio.poje.controller.project.projectDto.ProjectUpdateRequestDto;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.domain.project.ProjectImg;
import com.portfolio.poje.repository.PortfolioRepository;
import com.portfolio.poje.repository.ProjectImgRepository;
import com.portfolio.poje.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectService {

    private final PortfolioRepository portfolioRepository;
    private final ProjectRepository projectRepository;
    private final ProjectImgRepository projectImgRepository;
    private final FileHandler fileHandler;


    /**
     * 프로젝트 생성
     * @param projectCreateRequestDto
     * @param files
     * @param portfolioId
     */
    @Transactional
    public void enroll(ProjectCreateRequestDto projectCreateRequestDto, List<MultipartFile> files, Long portfolioId) throws Exception {
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        Project project = Project.createProject()
                .name(projectCreateRequestDto.getName())
                .duration(projectCreateRequestDto.getDuration())
                .description(portfolio.getDescription())
                .belong(projectCreateRequestDto.getBelong())
                .link(projectCreateRequestDto.getLink())
                .portfolio(portfolio)
                .build();

        List<ProjectImg> projectImgList = fileHandler.parseFileInfo(files);

        // 파일이 존재할 때만 처리
        if (!projectImgList.isEmpty()){
            for (ProjectImg projectImg : projectImgList){
                projectImgRepository.save(projectImg);
                projectImg.addProject(project);
            }
        }

        projectRepository.save(project);
    }


    @Transactional
    public void updateProject(ProjectUpdateRequestDto projectUpdateRequestDto, Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        project.updateInfo(projectUpdateRequestDto.getName(), projectUpdateRequestDto.getDuration(),
                            projectUpdateRequestDto.getDescription(), projectUpdateRequestDto.getBelong(),
                            projectUpdateRequestDto.getLink());

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
