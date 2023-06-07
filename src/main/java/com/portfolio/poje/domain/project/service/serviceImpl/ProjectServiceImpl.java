package com.portfolio.poje.domain.project.service.serviceImpl;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.domain.portfolio.repository.PortfolioRepository;
import com.portfolio.poje.domain.project.dto.PrDto;
import com.portfolio.poje.domain.project.dto.PrImgDto;
import com.portfolio.poje.domain.project.entity.Project;
import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import com.portfolio.poje.domain.project.repository.ProjectRepository;
import com.portfolio.poje.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final PortfolioRepository portfolioRepository;
    private final ProjectRepository projectRepository;
    private final ProjectAwardServiceImpl projectAwardServiceImpl;
    private final ProjectSkillServiceImpl projectSkillServiceImpl;
    private final ProjectImgServiceImpl projectImgServiceImpl;


    /**
     * 기본 프로젝트 생성
     * @param portfolioId
     * @return PrAllInfoResp
     */
    @Override
    @Transactional
    public PrDto.PrAllInfoResp enrollBasicProject(Long portfolioId){
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

        return PrDto.PrAllInfoResp.builder()
                .project(project)
                .prSkillList(projectSkillServiceImpl.toPrSkillListDto(project.getId()))
                .build();
    }


    /**
     * 포트폴리오의 프로젝트별 관련 정보 목록 반환
     * @param portfolioId
     * @return List<PrAllInfoResp>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PrDto.PrAllInfoResp> getProjectInfoList(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        List<PrDto.PrAllInfoResp> prList = new ArrayList<>();

        for (Project project : portfolio.getProjects()){
            prList.add(PrDto.PrAllInfoResp.builder()
                    .project(project)
                    .prSkillList(projectSkillServiceImpl.toPrSkillListDto(project.getId()))
                    .build());
        }

        return prList;
    }


    /**
     * 프로젝트 수정
     * @param projectId
     * @param prUpdateReq
     * @param prImgDelListReq
     * @param files
     * @return PrAllInfoResp
     * @throws IOException
     */
    @Override
    @Transactional
    public PrDto.PrAllInfoResp updateProject(Long projectId, PrDto.PrUpdateReq prUpdateReq,
                                             PrImgDto.PrImgDelListReq prImgDelListReq, List<MultipartFile> files) throws IOException {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        // 프로젝트 정보 수정
        project.updateInfo(prUpdateReq.getPrInfo().getName(), prUpdateReq.getPrInfo().getDuration(),
                            prUpdateReq.getPrInfo().getDescription(), prUpdateReq.getPrInfo().getBelong(),
                            prUpdateReq.getPrInfo().getLink());

        // 프로젝트 수상 정보 수정
        projectAwardServiceImpl.updateAwardInfo(project.getId(), prUpdateReq.getPrAwardInfo());
        // 프로젝트 사용 기술 수정
        projectSkillServiceImpl.updateProjectSkill(project.getId(), prUpdateReq.getSkillSet());
        // 프로젝트 이미지 수정
        projectImgServiceImpl.updateImages(project.getId(), prImgDelListReq, files);

        return PrDto.PrAllInfoResp.builder()
                .project(project)
                .prSkillList(projectSkillServiceImpl.toPrSkillListDto(project.getId()))
                .build();
    }


    /**
     * 프로젝트 삭제
     * @param projectId
     */
    @Override
    @Transactional
    public void deleteProject(Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        Portfolio portfolio = portfolioRepository.findById(project.getPortfolio().getId()).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        portfolio.getProjects().remove(project);
        projectRepository.delete(project);
    }

}
