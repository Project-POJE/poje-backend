package com.portfolio.poje.service.project;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.project.projectDto.PrAllInfoResp;
import com.portfolio.poje.controller.project.projectDto.PrDeleteReq;
import com.portfolio.poje.controller.project.projectDto.PrUpdateReq;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.repository.portfolio.PortfolioRepository;
import com.portfolio.poje.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectService {

    private final PortfolioRepository portfolioRepository;
    private final ProjectRepository projectRepository;
    private final ProjectAwardService projectAwardService;
    private final ProjectSkillService projectSkillService;
    private final ProjectImgService projectImgService;


    /**
     * 기본 프로젝트 생성
     * @param portfolioId
     * @return : PrAllInfoResp
     */
    @Transactional
    public PrAllInfoResp enrollBasicProject(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        Project project = Project.createProject()
                .name("제목을 입력해주세요.")
                .duration("기간을 입력해주세요.")
                .description("설명을 입력해주세요.")
                .belong("소속을 입력해주세요. (e.g. 토이 프로젝트, 팀 프로젝트)")
                .link("관련 링크를 입력해주세요.")
                .portfolio(portfolio)
                .build();

        projectRepository.save(project);

        return PrAllInfoResp.builder()
                .project(project)
                .prSkillList(projectSkillService.toPrSkillListDto(project.getId()))
                .build();
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
                    .prSkillList(projectSkillService.toPrSkillListDto(project.getId()))
                    .build());
        }

        return prList;
    }



    /**
     * 프로젝트 수정
     * @param projectId
     * @param prUpdateReq
     * @param files
     * @throws Exception
     */
    @Transactional
    public void updateProject(Long projectId, PrUpdateReq prUpdateReq, List<MultipartFile> files) throws Exception{
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        // 프로젝트 정보 수정
        project.updateInfo(prUpdateReq.getPrInfo().getName(), prUpdateReq.getPrInfo().getDuration(),
                            prUpdateReq.getPrInfo().getDescription(), prUpdateReq.getPrInfo().getBelong(),
                            prUpdateReq.getPrInfo().getLink());

        // 프로젝트 수상 정보 수정
        projectAwardService.updateAwardInfo(project.getId(), prUpdateReq.getPrAwardInfo());

        // 프로젝트 사용 기술 수정
        projectSkillService.updateProjectSkill(project.getId(), prUpdateReq.getSkillSet());

        // 프로젝트 이미지 수정
        projectImgService.updateImages(project.getId(), files);
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
