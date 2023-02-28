package com.portfolio.poje.domain.portfolio.controller;

import com.amazonaws.util.StringUtils;
import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.domain.portfolio.dto.PfDto;
import com.portfolio.poje.domain.portfolio.service.PortfolioService;
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
     * 포트폴리오 수정 권한 여부 응답
     * @param portfolioId
     * @return : boolean
     */
    @GetMapping("/portfolio/{portfolio_id}/permission")
    public ResponseEntity<BasicResponse> permissionToModify(@PathVariable(value = "portfolio_id") Long portfolioId){
        boolean permission = portfolioService.getPermission(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "수정 권한 여부 반환", permission));
    }

    /**
     * 기본 정보만 담은 포트폴리오 생성
     * @param jobName
     * @return pfCreateResp
     */
    @PostMapping("/portfolio")
    public ResponseEntity<BasicResponse> createBasicPortfolio(@RequestParam(value = "job") String jobName){
        PfDto.PfCreateResp pfCreateResp = portfolioService.enrollBasicPortfolio(jobName);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "기본 포트폴리오가 생성되었습니다.", pfCreateResp));
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
                                                             @RequestPart(value = "portfolioUpdateReq") @Valid PfDto.PfUpdateReq pfUpdateReq,
                                                             @RequestPart(value = "portfolioImg", required = false)MultipartFile multipartFile) throws Exception{
        PfDto.PfInfoResp pfInfoResp = portfolioService.updatePortfolioInfo(portfolioId, pfUpdateReq, multipartFile);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "포트폴리오 정보가 수정되었습니다.", pfInfoResp));
    }


    /**
     * 직무 별 포트폴리오 & 작성자 정보 목록 반환
     * @param jobName
     * @param keyword
     * @param page
     * @return : PfAndMemberListResp
     */
    @GetMapping("/portfolios")
    public ResponseEntity<BasicResponse> getPortfolios(@RequestParam(value = "name") String jobName,
                                                       @RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "page", required = false) Integer page){
        if (page == null || page < 1) page = 1;
        if (StringUtils.isNullOrEmpty(keyword)) keyword = "";


        PfDto.PfAndMemberListResp pfAndMemberListResp;
        if (jobName.equals("전체"))
            pfAndMemberListResp = portfolioService.getPortfolios(page, keyword);
        else
            pfAndMemberListResp = portfolioService.getPortfoliosWithJob(jobName, page, keyword);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "직무별 포트폴리오 목록 반환", pfAndMemberListResp));
    }


    /**
     * 포트폴리오 정보 반환
     * @param portfolioId
     * @return : PfInfoResp
     */
    @GetMapping("/portfolio/{portfolio_id}")
    public ResponseEntity<BasicResponse> getPortfolioInfo(@PathVariable(value = "portfolio_id") Long portfolioId){
        PfDto.PfInfoResp pfInfoResp = portfolioService.getPortfolioInfo(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "포트폴리오 정보 반환", pfInfoResp));
    }


    /**
     * 포트폴리오 About Me 정보 반환
     * @param portfolioId
     * @return : PfAboutMeResp
     */
    @GetMapping("/portfolio/{portfolio_id}/about-me")
    public ResponseEntity<BasicResponse> getPortfolioAboutMe(@PathVariable(value = "portfolio_id") Long portfolioId){
        PfDto.PfAboutMeResp pfAboutMeResp = portfolioService.getPortfolioAboutMe(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "About Me 정보 반환", pfAboutMeResp));
    }


    /**
     * 내 포트폴리오 정보 목록 반환
     * @return : PfAndMemberListResp
     */
    @GetMapping("/my-portfolios")
    public ResponseEntity<BasicResponse> getMemberPortfolios(){
        PfDto.PfAndMemberListResp pfAndMemberListResp = portfolioService.getMyPortfolios();

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "내 포트폴리오 목록 반환", pfAndMemberListResp));
    }


    /**
     * 포트폴리오 삭제
     * @param portfolioId
     * @return
     */
    @DeleteMapping("/portfolio/{portfolio_id}")
    public ResponseEntity<BasicResponse> deletePortfolio(@PathVariable(value = "portfolio_id") Long portfolioId){
        portfolioService.deletePortfolio(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "포트폴리오 삭제 성공"));
    }

}
