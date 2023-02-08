package com.portfolio.poje.controller.portfolio;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.portfolio.portfolioDto.PfAndMemberListResp;
import com.portfolio.poje.controller.portfolio.portfolioDto.PfInfoResp;
import com.portfolio.poje.controller.portfolio.portfolioDto.PfUpdateReq;
import com.portfolio.poje.service.portfolio.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping(value = "/member")
@RestController
public class PortfolioController {

    private final PortfolioService portfolioService;

    /**
     * 기본 정보만 담은 포트폴리오 생성
     * @param jobName
     */
    @PostMapping("/portfolio")
    public ResponseEntity<BasicResponse> createBasicPortfolio(@RequestParam(value = "job") String jobName){
        portfolioService.enrollBasicPortfolio(jobName);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "기본 포트폴리오가 생성되었습니다."));
    }


    /**
     * 포트폴리오 정보 수정
     * @param portfolioId
     * @param pfUpdateReq
     * @param multipartFile
     * @return : PfInfoResp
     */
    @PutMapping("/portfolio/{portfolio_id}")
    public ResponseEntity<BasicResponse> updatePortfolioInfo(@PathVariable(value = "portfolio_id") Long portfolioId,
                                                             @RequestPart(value = "portfolioUpdateReq") @Valid PfUpdateReq pfUpdateReq,
                                                             @RequestPart(value = "portfolioImg", required = false)MultipartFile multipartFile) throws Exception{
        PfInfoResp pfInfoResp = portfolioService.updatePortfolioInfo(portfolioId, pfUpdateReq, multipartFile);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "포트폴리오 정보가 수정되었습니다.", pfInfoResp));
    }


    /**
     * 직무 별 포트폴리오 & 작성자 정보 목록 반환
     * @param jobName
     * @return : PfAndMemberListResp
     */
    @GetMapping("/portfolios")
    public ResponseEntity<BasicResponse> getPortfolios(@RequestParam(value = "name") String jobName){
        PfAndMemberListResp pfAndMemberListResp = portfolioService.getPortfoliosWithJob(jobName);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "직무별 포트폴리오 목록 반환", pfAndMemberListResp));
    }


    /**
     * 포트폴리오 정보 반환
     * @param portfolioId
     * @return : PfInfoResp
     */
    @GetMapping("/portfolio/{portfolio_id}")
    public ResponseEntity<BasicResponse> getPortfolioInfo(@PathVariable(value = "portfolio_id") Long portfolioId){
        PfInfoResp pfInfoResp = portfolioService.getPortfolioInfo(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "포트폴리오 정보 반환", pfInfoResp));
    }


}
