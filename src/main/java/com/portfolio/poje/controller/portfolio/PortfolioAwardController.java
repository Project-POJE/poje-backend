package com.portfolio.poje.controller.portfolio;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PfAwardCreateReq;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PfAwardDeleteReq;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PfAwardUpdateReq;
import com.portfolio.poje.service.portfolio.PortfolioAwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class PortfolioAwardController {

    private final PortfolioAwardService portfolioAwardService;


    /**
     * 포트폴리오 수상 정보 등록
     * @param pfAwardCreateReq
     * @return
     */
    @PostMapping("/portfolio/award")
    public ResponseEntity<BasicResponse> createPortfolioAward(@RequestBody PfAwardCreateReq pfAwardCreateReq){
        portfolioAwardService.enroll(pfAwardCreateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "등록되었습니다."));
    }


    /**
     * 포트폴리오 수상 정보 수정
     * @param pfAwardUpdateReq
     * @return
     */
    @PutMapping("/portfolio/award")
    public ResponseEntity<BasicResponse> updatePortfolioAward(@RequestBody PfAwardUpdateReq pfAwardUpdateReq){
        portfolioAwardService.updateAwardInfo(pfAwardUpdateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "수정되었습니다."));
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