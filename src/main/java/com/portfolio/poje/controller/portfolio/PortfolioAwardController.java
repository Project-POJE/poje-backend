package com.portfolio.poje.controller.portfolio;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PfAwardDeleteReq;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PfAwardInfoResp;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PfAwardUpdateReq;
import com.portfolio.poje.service.portfolio.PortfolioAwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class PortfolioAwardController {

    private final PortfolioAwardService portfolioAwardService;


    /**
     * 포트폴리오 기본 수상 정보 생성
     * @param portfolioId
     * @return : PfAwardInfoResp
     */
    @PostMapping("/portfolio/{portfolio_id}/award")
    public ResponseEntity<BasicResponse> createPortfolioAward(@PathVariable(value = "portfolio_id") Long portfolioId){
        PfAwardInfoResp pfAwardInfoResp = portfolioAwardService.createAward(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "등록되었습니다.", pfAwardInfoResp));
    }


    /**
     * 포트폴리오 수상 정보 목록 반환
     * @param portfolioId
     * @return
     */
    @GetMapping("/portfolio/{portfolio_id}/awards")
    public ResponseEntity<BasicResponse> getPortfolioAwards(@PathVariable(value = "portfolio_id") Long portfolioId){
        List<PfAwardInfoResp> pfAwardInfoRespList = portfolioAwardService.getPortfolioAwardList(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "포트폴리오 수상 정보 목록 반환", pfAwardInfoRespList));
    }



    /**
     * 포트폴리오 수상 정보 수정
     * @param pfAwardUpdateReq
     * @return :PfAwardInfoResp
     */
    @PutMapping("/portfolio/award")
    public ResponseEntity<BasicResponse> updatePortfolioAward(@RequestBody PfAwardUpdateReq pfAwardUpdateReq){
        PfAwardInfoResp pfAwardInfoResp = portfolioAwardService.updateAwardInfo(pfAwardUpdateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "수정되었습니다.", pfAwardInfoResp));
    }


    /**
     * 포트폴리오 수상 정보 삭제
     * @param pfAwardDeleteReq
     * @return
     */
    @DeleteMapping("/portfolio/award")
    public ResponseEntity<BasicResponse> deletePortfolioAward(@RequestBody PfAwardDeleteReq pfAwardDeleteReq){
        portfolioAwardService.deleteAward(pfAwardDeleteReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "삭제되었습니다."));
    }


}
