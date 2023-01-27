package com.portfolio.poje.service.portfolio;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PfSkillCreateReq;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PfSkillInfoReq;
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
     * @param pfSkillCreateReq
     */
    @Transactional
    public void enroll(PfSkillCreateReq pfSkillCreateReq){
        Portfolio portfolio = portfolioRepository.findById(pfSkillCreateReq.getPortfolioId()).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        for (PfSkillInfoReq skillInfo: pfSkillCreateReq.getSkills()){
            PortfolioSkill portfolioSkill = PortfolioSkill.builder()
                    .type(skillInfo.getType())
                    .skill(skillInfo.getSkill())
                    .portfolio(portfolio)
                    .build();

            portfolioSkillRepository.save(portfolioSkill);
        }

    }


    /**
     * 포트폴리오 사용 기술 삭제
     * @param portfolioId
     * @param skillId
     */
    @Transactional
    public void deletePortfolioSkill(Long portfolioId, Long skillId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        PortfolioSkill portfolioSkill = portfolioSkillRepository.findById(skillId).orElseThrow(
                () -> new PojeException(ErrorCode.SKILL_NOT_FOUND)
        );

        portfolio.getPortfolioSkills().remove(portfolioSkill);
        portfolioSkillRepository.delete(portfolioSkill);
    }



}
