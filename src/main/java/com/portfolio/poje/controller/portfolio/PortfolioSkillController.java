package com.portfolio.poje.controller.portfolio;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PortfolioSkillCreateRequest;
import com.portfolio.poje.service.portfolio.PortfolioSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class PortfolioSkillController {

    private final PortfolioSkillService portfolioSkillService;


    /**
     * 포트폴리오 사용 기술 추가
     * @param portfolioSkillCreateRequest
     * @return
     */
    @PostMapping("/portfolio/skill")
    public ResponseEntity<BasicResponse> createPortfolioSkill(@RequestBody PortfolioSkillCreateRequest portfolioSkillCreateRequest){
        portfolioSkillService.enroll(portfolioSkillCreateRequest);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "추가되었습니다."));
    }

}
