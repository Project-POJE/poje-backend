package com.portfolio.poje.domain.portfolio.controller;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.domain.portfolio.dto.portfolioSkillDto.PfSkillListResp;
import com.portfolio.poje.domain.portfolio.dto.portfolioSkillDto.PfSkillCreateReq;
import com.portfolio.poje.domain.portfolio.dto.portfolioSkillDto.PfSkillUpdateReq;
import com.portfolio.poje.domain.portfolio.service.PortfolioSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class PortfolioSkillController {

    private final PortfolioSkillService portfolioSkillService;


    /**
     * 포트폴리오 사용 기술 추가
     * @param portfolioId
     * @param pfSkillCreateReq
     * @return
     */
    @PostMapping("/portfolio/{portfolio_id}/skill")
    public ResponseEntity<BasicResponse> createPortfolioSkill(@PathVariable(value = "portfolio_id") Long portfolioId,
                                                              @RequestBody PfSkillCreateReq pfSkillCreateReq){
        portfolioSkillService.enroll(portfolioId, pfSkillCreateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "추가되었습니다."));
    }


    /**
     * 포트폴리오 사용 기술 수정 (추가 or 삭제)
     * @param portfolioId
     * @param pfSkillUpdateReq
     * @return : List<PfSkillListResp>
     */
    @PutMapping("/portfolio/{portfolio_id}/skill")
    public ResponseEntity<BasicResponse> updatePortfolioSkill(@PathVariable(value = "portfolio_id") Long portfolioId,
                                                              @RequestBody PfSkillUpdateReq pfSkillUpdateReq){
        portfolioSkillService.updatePortfolioSkill(portfolioId, pfSkillUpdateReq);

        List<PfSkillListResp> pfSkillListRespList = portfolioSkillService.getPortfolioSkills(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "수정되었습니다.", pfSkillListRespList));
    }


    /**
     * 포트폴리오 사용 기술 목록 반환
     * @param portfolioId
     * @return : List<PfSkillListResp>
     */
    @GetMapping("/portfolio/{portfolio_id}/skills")
    public ResponseEntity<BasicResponse> getPortfolioSkills(@PathVariable(value = "portfolio_id") Long portfolioId){
        List<PfSkillListResp> pfSkillListRespList = portfolioSkillService.getPortfolioSkills(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "포트폴리오 사용 기술 목록 반환", pfSkillListRespList));
    }


}
