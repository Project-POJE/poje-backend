package com.portfolio.poje.service.portfolio;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PortfolioSkillCreateRequest;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PortfolioSkillInfoRequest;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.portfolio.PortfolioSkill;
import com.portfolio.poje.repository.portfolio.PortfolioRepository;
import com.portfolio.poje.repository.portfolio.PortfolioSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PortfolioSkillService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioSkillRepository portfolioSkillRepository;


    /**
     * 포트폴리오 사용 기술 추가
     * @param portfolioSkillCreateRequest
     */
    @Transactional
    public void enroll(PortfolioSkillCreateRequest portfolioSkillCreateRequest){
        Portfolio portfolio = portfolioRepository.findById(portfolioSkillCreateRequest.getPortfolioId()).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        for (PortfolioSkillInfoRequest skillInfo: portfolioSkillCreateRequest.getSkills()){
            PortfolioSkill portfolioSkill = PortfolioSkill.builder()
                    .type(skillInfo.getType())
                    .skill(skillInfo.getSkill())
                    .portfolio(portfolio)
                    .build();

            portfolioSkillRepository.save(portfolioSkill);
        }

    }



}
