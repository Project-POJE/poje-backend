package com.portfolio.poje.domain.portfolio.service;

import com.portfolio.poje.domain.portfolio.dto.PfDto;
import com.portfolio.poje.domain.portfolio.dto.PfLikeDto;

public interface PortfolioLikeService {

    /**
     * 포트폴리오 '좋아요' 클릭 (이미 '좋아요'를 눌렀으면 취소)
     * @param portfolioId
     * @return PfLikeInfoResp
     */
    PfLikeDto.PfLikeInfoResp heartPortfolio(Long portfolioId);


    /**
     * 좋아요 누른 포트폴리오 목록 반환
     * @param page
     * @return PfAndMemberListResp
     */
    PfDto.PfAndMemberListResp likePortfolios(int page);
}
