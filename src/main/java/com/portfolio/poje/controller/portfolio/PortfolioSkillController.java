package com.portfolio.poje.controller.portfolio;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PfSkillCreateReq;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PfSkillDeleteReq;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PfSkillListResp;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PfSkillUpdateReq;
import com.portfolio.poje.service.portfolio.PortfolioSkillService;
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


    /**
     * 포트폴리오 사용 기술 삭제
     * @param pfSkillDeleteReq
     * @return
     */
    @DeleteMapping("/portfolio/skill")
    public ResponseEntity<BasicResponse> deletePortfolioSkill(@RequestBody PfSkillDeleteReq pfSkillDeleteReq){
        portfolioSkillService.deletePortfolioSkill(pfSkillDeleteReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "삭제되었습니다."));
    }

}
