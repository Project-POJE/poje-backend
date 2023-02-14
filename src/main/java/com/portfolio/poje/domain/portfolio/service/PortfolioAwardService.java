package com.portfolio.poje.domain.portfolio.service;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.domain.portfolio.dto.PfAwardDto;
import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import com.portfolio.poje.domain.portfolio.entity.PortfolioAward;
import com.portfolio.poje.domain.portfolio.repository.PortfolioAwardRepository;
import com.portfolio.poje.domain.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PortfolioAwardService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioAwardRepository portfolioAwardRepository;


    /**
     * 포트폴리오 기본 수상 정보 생성
     * @param portfolioId
     * @return : PfAwardInfoResp
     */
    @Transactional
    public PfAwardDto.PfAwardInfoResp createAward(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        PortfolioAward portfolioAward = PortfolioAward.enrollPortfolioAward()
                .supervision("주최를 입력해주세요.")
                .grade("순위를 입력해주세요. (e.g.3등 or 동상)")
                .description("설명을 입력해주세요.")
                .portfolio(portfolio)
                .build();

        portfolioAwardRepository.save(portfolioAward);

        return PfAwardDto.PfAwardInfoResp.builder()
                .portfolioAward(portfolioAward)
                .build();
    }


    /**
     * 포트폴리오 수상 정보 목록 반환
     * @param portfolioId
     * @return : List<PfAwardInfoResp>
     */
    @Transactional(readOnly = true)
    public List<PfAwardDto.PfAwardInfoResp> getPortfolioAwardList(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        return portfolio.getPortfolioAwards().stream()
                .map(award -> PfAwardDto.PfAwardInfoResp.builder()
                        .portfolioAward(award)
                        .build())
                .collect(Collectors.toList());
    }


    /**
     * 포트폴리오 수상 정보 수정
     * @param pfAwardUpdateReq
     */
    @Transactional
    public PfAwardDto.PfAwardInfoResp updateAwardInfo(Long awardId, PfAwardDto.PfAwardUpdateReq pfAwardUpdateReq){
        PortfolioAward portfolioAward = portfolioAwardRepository.findById(awardId).orElseThrow(
                () -> new PojeException(ErrorCode.AWARD_NOT_FOUND)
        );

        portfolioAward.updateInfo(pfAwardUpdateReq.getSupervision(),
                                  pfAwardUpdateReq.getGrade(),
                                  pfAwardUpdateReq.getDescription());

        return PfAwardDto.PfAwardInfoResp.builder()
                .portfolioAward(portfolioAward)
                .build();
    }


    /**
     * 포트폴리오 수상 정보 삭제
     * @param awardId
     */
    @Transactional
    public void deleteAward(Long awardId){
        PortfolioAward portfolioAward = portfolioAwardRepository.findById(awardId).orElseThrow(
                () -> new PojeException(ErrorCode.AWARD_NOT_FOUND)
        );

        Portfolio portfolio = portfolioRepository.findById(portfolioAward.getPortfolio().getId()).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        portfolio.getPortfolioAwards().remove(portfolioAward);
        portfolioAwardRepository.delete(portfolioAward);
    }


}
