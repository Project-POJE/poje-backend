package com.portfolio.poje.controller.portfolio;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.portfolio.portfolioDto.PortfolioBasicInfoResponse;
import com.portfolio.poje.controller.portfolio.portfolioDto.PortfolioInfoResponse;
import com.portfolio.poje.service.portfolio.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping(value = "/member")
@RestController
public class PortfolioController {

    private final PortfolioService portfolioService;

    /**
     * 기본 정보만 담은 포트폴리오 생성
     * @param jobMap
     * @return : PortfolioBasicInfoResponse
     */
    @PostMapping("/portfolio")
    public ResponseEntity<BasicResponse> createBasicPortfolio(@RequestBody Map<String, Long> jobMap){
        PortfolioBasicInfoResponse portfolioBasicInfoResponse = portfolioService.enrollBasicPortfolio(jobMap.get("jobId"));

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "기본 포트폴리오가 생성되었습니다.", portfolioBasicInfoResponse));
    }


    /**
     * 포트폴리오 관련 정보 반환
     * @param portfolioId
     * @return : PortfolioInfoResponse
     */
    @GetMapping("/portfolio/{portfolio_id}")
    public ResponseEntity<BasicResponse> portfolioInfo(@PathVariable(value = "portfolio_id") Long portfolioId){
        PortfolioInfoResponse portfolioInfoResponse = portfolioService.portfolioInfo(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "포트폴리오 정보 반환", portfolioInfoResponse));
    }




}
