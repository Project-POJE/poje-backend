package com.portfolio.poje.domain.portfolio.service;

import com.portfolio.poje.domain.portfolio.dto.PfSkillDto;

import java.util.List;

public interface PortfolioSkillService {

    /**
     * 포트폴리오 사용 기술 추가
     * @param portfolioId
     * @param pfSkillCreateReq
     */
    void enroll(Long portfolioId, PfSkillDto.PfSkillCreateReq pfSkillCreateReq);


    /**
     * 포트폴리오 사용 기술 수정 (추가 or 삭제)
     * @param portfolioId
     * @param pfSkillUpdateReq
     */
    void updatePortfolioSkill(Long portfolioId, PfSkillDto.PfSkillUpdateReq pfSkillUpdateReq);


    /**
     * 포트폴리오 사용 기술 목록 반환
     * @param portfolioId
     * @return List<PfSkillListResp>
     */
    List<PfSkillDto.PfSkillListResp> getPortfolioSkills(Long portfolioId);
}
