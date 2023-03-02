package com.portfolio.poje.domain.portfolio.controller;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.domain.portfolio.dto.PfDto;
import com.portfolio.poje.domain.portfolio.dto.PfLikeDto;
import com.portfolio.poje.domain.portfolio.service.PortfolioLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class PortfolioLikeController {

    private final PortfolioLikeService portfolioLikeService;


    /**
     * 포트폴리오 '좋아요' 클릭
     * @param portfolioId
     * @return : PfLikeInfoResp
     */
    @PostMapping("/portfolio/{portfolio_id}/heart")
    public ResponseEntity<BasicResponse> likePortfolio(@PathVariable(value = "portfolio_id") Long portfolioId){
        PfLikeDto.PfLikeInfoResp pfLikeInfoResp = portfolioLikeService.heartPortfolio(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "좋아요를 눌렀습니다", pfLikeInfoResp));
    }


    /**
     * 좋아요 누른 포트폴리오 목록 반환
     * @param page
     * @return : PfAndMemberListResp
     */
    @GetMapping("/like/portfolios")
    public ResponseEntity<BasicResponse> likePortfolios(@RequestParam(value = "page", required = false) Integer page){
        if (page == null || page < 1) page = 1;

        PfDto.PfAndMemberListResp pfAndMemberListResp = portfolioLikeService.likePortfolios(page);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "좋아요 누른 포트폴리오 목록 반환", pfAndMemberListResp));
    }

}
