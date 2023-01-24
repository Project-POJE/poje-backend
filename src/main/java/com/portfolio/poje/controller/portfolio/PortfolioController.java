package com.portfolio.poje.controller.portfolio;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.portfolio.portfolioDto.PortfolioCreateRequestDto;
import com.portfolio.poje.controller.portfolio.portfolioDto.PortfolioInfoResponseDto;
import com.portfolio.poje.service.portfolio.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(value = "/member")
@RestController
public class PortfolioController {

    private final PortfolioService portfolioService;


    /**
     * 포트폴리오 생성
     * @param portfolioCreateRequestDto
     * @return
     */
    @PostMapping("/portfolio")
    public ResponseEntity<BasicResponse> enroll(@RequestBody PortfolioCreateRequestDto portfolioCreateRequestDto){
        portfolioService.createPortfolio(portfolioCreateRequestDto);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "생성되었습니다."));
    }


    /**
     * 포트폴리오 관련 정보 반환
     * @param portfolioId
     * @return : PortfolioInfoResponseDto
     */
    @GetMapping("/portfolio/{portfolio_id}")
    public ResponseEntity<BasicResponse> portfolioInfo(@PathVariable(value = "portfolio_id") Long portfolioId){
        PortfolioInfoResponseDto portfolioInfoResponseDto = portfolioService.portfolioInfo(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "포트폴리오 정보 반환", portfolioInfoResponseDto));
    }




}
