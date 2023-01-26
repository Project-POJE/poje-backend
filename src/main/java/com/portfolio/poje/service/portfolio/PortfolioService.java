package com.portfolio.poje.service.portfolio;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.controller.portfolio.portfolioDto.PortfolioBasicInfoResponse;
import com.portfolio.poje.controller.portfolio.portfolioDto.PortfolioInfoResponse;
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
     * @return : PortfolioBasicInfoResponse
     */
    @Transactional
    public PortfolioBasicInfoResponse enrollBasicPortfolio(Long jobId){
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

        return PortfolioBasicInfoResponse.builder()
                .portfolio(portfolio)
                .build();
    }


    /**
     * 포트폴리오 정보 반환
     * @param portfolioId
     * @return : PortfolioInfoResponse
     */
    @Transactional(readOnly = true)
    public PortfolioInfoResponse portfolioInfo(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        return PortfolioInfoResponse.builder()
                .portfolio(portfolio)
                .build();
    }
}
