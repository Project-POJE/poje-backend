package com.portfolio.poje.domain.portfolio.controller;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.domain.portfolio.dto.PfAwardDto;
import com.portfolio.poje.domain.portfolio.service.PortfolioAwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        PfAwardDto.PfAwardInfoResp pfAwardInfoResp = portfolioAwardService.createAward(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "등록되었습니다.", pfAwardInfoResp));
    }


    /**
     * 포트폴리오 수상 정보 목록 반환
     * @param portfolioId
     * @return
     */
    @GetMapping("/portfolio/{portfolio_id}/awards")
    public ResponseEntity<BasicResponse> getPortfolioAwards(@PathVariable(value = "portfolio_id") Long portfolioId){
        List<PfAwardDto.PfAwardInfoResp> pfAwardInfoRespList = portfolioAwardService.getPortfolioAwardList(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "포트폴리오 수상 정보 목록 반환", pfAwardInfoRespList));
    }



    /**
     * 포트폴리오 수상 정보 수정
     * @param awardId
     * @param pfAwardUpdateReq
     * @return :PfAwardInfoResp
     */
    @PutMapping("/portfolio/award/{award_id}")
    public ResponseEntity<BasicResponse> updatePortfolioAward(@PathVariable(value = "award_id") Long awardId,
                                                              @RequestBody @Valid PfAwardDto.PfAwardUpdateReq pfAwardUpdateReq){
        PfAwardDto.PfAwardInfoResp pfAwardInfoResp = portfolioAwardService.updateAwardInfo(awardId, pfAwardUpdateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "수정되었습니다.", pfAwardInfoResp));
    }


    /**
     * 포트폴리오 수상 정보 삭제
     * @param awardId
     * @return
     */
    @DeleteMapping("/portfolio/award/{award_id}")
    public ResponseEntity<BasicResponse> deletePortfolioAward(@PathVariable(value = "award_id") Long awardId){
        portfolioAwardService.deleteAward(awardId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "삭제되었습니다."));
    }


}
