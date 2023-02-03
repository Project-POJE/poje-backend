package com.portfolio.poje.service.portfolio;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.controller.portfolio.portfolioDto.PfAndMemberListResp;
import com.portfolio.poje.controller.portfolio.portfolioDto.PfInfoResp;
import com.portfolio.poje.domain.ability.Job;
import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.repository.ability.JobRepository;
import com.portfolio.poje.repository.member.MemberRepository;
import com.portfolio.poje.repository.portfolio.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;
    private final JobRepository jobRepository;


    /**
     * 기본 정보만 담은 포트폴리오 생성
     * @param jobId
     */
    @Transactional
    public void enrollBasicPortfolio(Long jobId){
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
    public PfInfoResp getPortfolioInfo(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        return PfInfoResp.builder()
                .portfolio(portfolio)
                .build();
    }

}
