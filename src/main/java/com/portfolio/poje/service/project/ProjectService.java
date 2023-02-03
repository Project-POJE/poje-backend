package com.portfolio.poje.service.project;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.project.projectDto.PrAllInfoResp;
import com.portfolio.poje.controller.project.projectDto.PrBasicInfoResp;
import com.portfolio.poje.controller.project.projectDto.PrDeleteReq;
import com.portfolio.poje.controller.project.projectDto.PrUpdateReq;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillInfoResp;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillListResp;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.domain.project.ProjectSkill;
import com.portfolio.poje.repository.portfolio.PortfolioRepository;
import com.portfolio.poje.repository.project.ProjectRepository;
import com.portfolio.poje.repository.project.ProjectSkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectService {

    private final PortfolioRepository portfolioRepository;
    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;


    /**
     * 기본 프로젝트 생성
     * @param portfolioId
     * @return
     */
    @Transactional
    public PrBasicInfoResp enrollBasicProject(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        Project project = Project.createProject()
                .portfolio(portfolio)
                .build();

        projectRepository.save(project);

        return new PrBasicInfoResp(project.getId());
    }


    /**
     * 포트폴리오의 프로젝트별 관련 정보 목록 반환
     * @param portfolioId
     * @return
     */
    @Transactional(readOnly = true)
    public List<PrAllInfoResp> getProjectInfoList(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        List<PrAllInfoResp> prList = new ArrayList<>();

        for (Project project : portfolio.getProjects()){
            prList.add(PrAllInfoResp.builder()
                    .project(project)
                    .prSkillList(toPrSkillListDto(project.getId()))
                    .build());
        }

        return prList;
    }


    /**
     * 프로젝트 사용 기술 타입별 정렬
     * @return : List<PrSkillListResp>
     */
    @Transactional(readOnly = true)
    public List<PrSkillListResp> toPrSkillListDto(Long projectId){

        List<PrSkillListResp> prSkillList = new ArrayList<>();

        List<String> skillTypeList = projectSkillRepository.findDistinctTypeById(projectId);
        for (String type : skillTypeList){
            List<PrSkillInfoResp> prSkillInfoList = new ArrayList<>();

            List<ProjectSkill> skills = projectSkillRepository.findByProjectIdAndType(projectId, type);
            for (ProjectSkill skill : skills){
                prSkillInfoList.add(new PrSkillInfoResp(skill.getId(), skill.getName()));
            }
            prSkillList.add(new PrSkillListResp(type, prSkillInfoList));
        }

        return prSkillList;
    }


    /**
     * 프로젝트 수정
     * @param prUpdateReq
     */
    @Transactional
    public void updateProject(PrUpdateReq prUpdateReq){
        Project project = projectRepository.findById(prUpdateReq.getProjectId()).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        project.updateInfo(prUpdateReq.getName(), prUpdateReq.getDuration(),
                            prUpdateReq.getDescription(), prUpdateReq.getBelong(),
                            prUpdateReq.getLink());

    }


    /**
     * 프로젝트 삭제
     * @param prDeleteReq
     */
    @Transactional
    public void deleteProject(PrDeleteReq prDeleteReq){
        Portfolio portfolio = portfolioRepository.findById(prDeleteReq.getPortfolioId()).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        Project project = projectRepository.findById(prDeleteReq.getProjectId()).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        portfolio.getProjects().remove(project);
        projectRepository.delete(project);
    }

}
