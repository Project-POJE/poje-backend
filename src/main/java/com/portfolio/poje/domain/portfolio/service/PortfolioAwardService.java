package com.portfolio.poje.domain.portfolio.service;

import com.portfolio.poje.domain.portfolio.dto.PfAwardDto;

import java.util.List;

public interface PortfolioAwardService {

    /**
     * 포트폴리오 기본 수상 정보 생성
     * @param portfolioId
     * @return PfAwardInfoResp
     */
    PfAwardDto.PfAwardInfoResp createAward(Long portfolioId);


    /**
     * 포트폴리오 수상 정보 목록 반환
     * @param portfolioId
     * @return List<PfAwardInfoResp>
     */
    List<PfAwardDto.PfAwardInfoResp> getPortfolioAwardList(Long portfolioId);


    /**
     * 포트폴리오 수상 정보 수정
     * @param awardId
     * @param pfAwardUpdateReq
     * @return PfAwardInfoResp
     */
    PfAwardDto.PfAwardInfoResp updateAwardInfo(Long awardId, PfAwardDto.PfAwardUpdateReq pfAwardUpdateReq);


    /**
     * 포트폴리오 수상 정보 삭제
     * @param awardId
     */
    void deleteAward(Long awardId);
}
