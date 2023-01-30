package com.portfolio.poje.service.portfolio;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.controller.portfolio.portfolioDto.PfAllInfoResp;
import com.portfolio.poje.controller.portfolio.portfolioDto.PfAndMemberListResp;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PfSkillInfoResp;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PfSkillListResp;
import com.portfolio.poje.controller.project.projectDto.PrAllInfoResp;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillInfoResp;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillListResp;
import com.portfolio.poje.domain.ability.Job;
import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.portfolio.PortfolioSkill;
import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.domain.project.ProjectSkill;
import com.portfolio.poje.repository.ability.JobRepository;
import com.portfolio.poje.repository.member.MemberRepository;
import com.portfolio.poje.repository.portfolio.PortfolioRepository;
import com.portfolio.poje.repository.portfolio.PortfolioSkillRepository;
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
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioSkillRepository portfolioSkillRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final MemberRepository memberRepository;
    private final JobRepository jobRepository;


    /**
     * 기본 정보만 담은 포트폴리오 생성
     * @param jobId
     * @return : PfInfoResp
     */
    @Transactional
    public PfAllInfoResp enrollBasicPortfolio(Long jobId){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new PojeException(ErrorCode.JOB_NOT_FOUND)
        );

        Portfolio portfolio = Portfolio.createPortfolio()
                .writer(member)
                .job(job)
                .build();

        portfolioRepository.save(portfolio);

        return PfAllInfoResp.builder()
                .portfolio(portfolio)
                .pfSkillList(toPfSkillListDto(portfolio.getId()))
                .prList(toPrAllInfoListDto(portfolio))
                .build();
    }


    /**
     * 직무 별 포트폴리오 & 작성자 정보 목록 반환
     * @param jobId
     * @return : PfAndMemberListResp
     */
    @Transactional(readOnly = true)
    public PfAndMemberListResp getPortfoliosWithJob(Long jobId){
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new PojeException(ErrorCode.JOB_NOT_FOUND)
        );

        return PfAndMemberListResp.builder()
                .job(job)
                .build();
    }


    /**
     * 포트폴리오 정보 반환
     * @param portfolioId
     * @return : PfInfoResp
     */
    @Transactional(readOnly = true)
    public PfAllInfoResp portfolioInfo(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        return PfAllInfoResp.builder()
                .portfolio(portfolio)
                .pfSkillList(toPfSkillListDto(portfolio.getId()))
                .prList(toPrAllInfoListDto(portfolio))
                .build();
    }


    /**
     * 포트폴리오 사용 기술 타입별 정렬
     * @return : List<PfSkillListResp>
     */
    @Transactional(readOnly = true)
    public List<PfSkillListResp> toPfSkillListDto(Long portfolioId){
        List<PfSkillListResp> pfSkillList = new ArrayList<>();

        // portfolioId로 해당 포트폴리오에서 사용하는 기술들의 type 목록을 가져옴
        List<String> skillTypeList = portfolioSkillRepository.findDistinctTypeById(portfolioId);
        for (String type : skillTypeList){
            List<PfSkillInfoResp> pfSkillInfoList = new ArrayList<>();

            List<PortfolioSkill> skills = portfolioSkillRepository.findByPortfolioIdAndType(portfolioId, type);
            for (PortfolioSkill skill : skills){
                pfSkillInfoList.add(new PfSkillInfoResp(skill.getName(), skill.getPath()));
            }
            pfSkillList.add(new PfSkillListResp(type, pfSkillInfoList));
        }

        return pfSkillList;
    }


    /**
     * 포트폴리오의 프로젝트별 관련 정보 목록 반환
     * @param portfolio
     * @return
     */
    @Transactional(readOnly = true)
    public List<PrAllInfoResp> toPrAllInfoListDto(Portfolio portfolio){
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
                prSkillInfoList.add(new PrSkillInfoResp(skill.getName()));
            }
            prSkillList.add(new PrSkillListResp(type, prSkillInfoList));
        }

        return prSkillList;
    }

}
