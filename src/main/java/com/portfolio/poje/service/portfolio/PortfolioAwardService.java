package com.portfolio.poje.service.portfolio;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PfAwardCreateReq;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PfAwardDeleteReq;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PfAwardUpdateReq;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.portfolio.PortfolioAward;
import com.portfolio.poje.repository.portfolio.PortfolioAwardRepository;
import com.portfolio.poje.repository.portfolio.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PortfolioAwardService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioAwardRepository portfolioAwardRepository;


    /**
     * 포트폴리오 수상 정보 등록
     * @param pfAwardCreateReq
     */
    @Transactional
    public void enroll(PfAwardCreateReq pfAwardCreateReq){
        Portfolio portfolio = portfolioRepository.findById(pfAwardCreateReq.getPortfolioId()).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        PortfolioAward portfolioAward = PortfolioAward.enrollPortfolioAward()
                .supervision(pfAwardCreateReq.getSupervision())
                .grade(pfAwardCreateReq.getGrade())
                .description(pfAwardCreateReq.getDescription())
                .portfolio(portfolio)
                .build();

        portfolioAwardRepository.save(portfolioAward);
    }


    /**
     * 포트폴리오 수상 정보 수정
     * @param pfAwardUpdateReq
     */
    @Transactional
    public void updateAwardInfo(PfAwardUpdateReq pfAwardUpdateReq){
        PortfolioAward portfolioAward = portfolioAwardRepository.findById(pfAwardUpdateReq.getPortfolioAwardId()).orElseThrow(
                () -> new PojeException(ErrorCode.AWARD_NOT_FOUND)
        );

        portfolioAward.updateInfo(pfAwardUpdateReq.getSupervision(),
                                  pfAwardUpdateReq.getGrade(),
                                  pfAwardUpdateReq.getDescription());


    }


    /**
     * 포트폴리오 수상 정보 삭제
     * @param pfAwardDeleteReq
     */
    @Transactional
    public void deleteAward(PfAwardDeleteReq pfAwardDeleteReq){
        Portfolio portfolio = portfolioRepository.findById(pfAwardDeleteReq.getPortfolioId()).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        PortfolioAward portfolioAward = portfolioAwardRepository.findById(pfAwardDeleteReq.getPortfolioAwardId()).orElseThrow(
                () -> new PojeException(ErrorCode.AWARD_NOT_FOUND)
        );

        portfolio.getPortfolioAwards().remove(portfolioAward);
        portfolioAwardRepository.delete(portfolioAward);
    }


}
