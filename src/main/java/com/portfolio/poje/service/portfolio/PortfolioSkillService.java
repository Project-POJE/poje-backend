package com.portfolio.poje.service.portfolio;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.*;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.portfolio.PortfolioSkill;
import com.portfolio.poje.repository.portfolio.PortfolioRepository;
import com.portfolio.poje.repository.portfolio.PortfolioSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

        for (PfSkillListReq skillSet : pfSkillCreateReq.getSkillSet()){
            for (PfSkillInfoReq skillInfo : skillSet.getSkills()){
                PortfolioSkill portfolioSkill = PortfolioSkill.builder()
                        .type(skillSet.getType())
                        .name(skillInfo.getName())
                        .path(skillInfo.getPath())
                        .portfolio(portfolio)
                        .build();

                portfolioSkillRepository.save(portfolioSkill);
            }
        }

    }


    /**
     * 포트폴리오 사용 기술 목록 반환
     * @param portfolioId
     * @return : List<PfSkillListResp>
     */
    @Transactional(readOnly = true)
    public List<PfSkillListResp> getPortfolioSkills(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        List<PfSkillListResp> pfSkillList = new ArrayList<>();

        // 포트폴리오에서 사용하는 기술들의 type 목록을 가져옴
        List<String> skillTypeList = portfolioSkillRepository.findDistinctTypeByPortfolio(portfolio);
        for (String type : skillTypeList){
            List<PfSkillInfoResp> pfSkillInfoList = new ArrayList<>();

            List<PortfolioSkill> skills = portfolioSkillRepository.findByPortfolioAndType(portfolio, type);
            for (PortfolioSkill skill : skills){
                pfSkillInfoList.add(new PfSkillInfoResp(skill.getId(), skill.getName(), skill.getPath()));
            }
            pfSkillList.add(new PfSkillListResp(type, pfSkillInfoList));
        }

        return pfSkillList;
    }


    /**
     * 포트폴리오 사용 기술 삭제
     * @param pfSkillDeleteReq
     */
    @Transactional
    public void deletePortfolioSkill(PfSkillDeleteReq pfSkillDeleteReq){
        Portfolio portfolio = portfolioRepository.findById(pfSkillDeleteReq.getPortfolioId()).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        PortfolioSkill portfolioSkill = portfolioSkillRepository.findById(pfSkillDeleteReq.getSkillId()).orElseThrow(
                () -> new PojeException(ErrorCode.SKILL_NOT_FOUND)
        );

        portfolio.getPortfolioSkills().remove(portfolioSkill);
        portfolioSkillRepository.delete(portfolioSkill);
    }


}
