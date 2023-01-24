package com.portfolio.poje.service.portfolio;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.controller.portfolio.portfolioDto.PortfolioCreateRequestDto;
import com.portfolio.poje.controller.portfolio.portfolioDto.PortfolioInfoResponseDto;
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
     * 포트폴리오 생성
     * @param portfolioCreateRequestDto
     */
    @Transactional
    public void createPortfolio(PortfolioCreateRequestDto portfolioCreateRequestDto){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Job job = jobRepository.findById(portfolioCreateRequestDto.getJobId()).orElseThrow(
                () -> new PojeException(ErrorCode.JOB_NOT_FOUND)
        );

        Portfolio portfolio = Portfolio.createPortfolio()
                .title(portfolioCreateRequestDto.getTitle())
                .description(portfolioCreateRequestDto.getDescription())
                .writer(member)
                .job(job)
                .gitHubLink(portfolioCreateRequestDto.getGitHubLink())
                .blogLink(portfolioCreateRequestDto.getBlogLink())
                .build();

        portfolioRepository.save(portfolio);
    }


    /**
     * 포트폴리오 정보 반환
     * @param portfolioId
     * @return : PortfolioInfoResponseDto
     */
    @Transactional(readOnly = true)
    public PortfolioInfoResponseDto portfolioInfo(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        return PortfolioInfoResponseDto.builder()
                .portfolio(portfolio)
                .build();
    }
}
