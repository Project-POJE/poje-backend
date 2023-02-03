package com.portfolio.poje.controller.project;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.project.projectDto.PrAllInfoResp;
import com.portfolio.poje.controller.project.projectDto.PrBasicInfoResp;
import com.portfolio.poje.controller.project.projectDto.PrDeleteReq;
import com.portfolio.poje.controller.project.projectDto.PrUpdateReq;
import com.portfolio.poje.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/member")
@RestController
public class ProjectController {

    private final ProjectService projectService;


    /**
     * 기본 프로젝트 생성
     * @param portfolioId
     * @return
     */
    @PostMapping("/portfolio/{portfolio_id}/project")
    public ResponseEntity<BasicResponse> createBasicProject(@PathVariable(value = "portfolio_id") Long portfolioId){
        PrBasicInfoResp prBasicInfoResp = projectService.enrollBasicProject(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "기본 프로젝트가 추가되었습니다", prBasicInfoResp));
    }


    /**
     * 프로젝트 정보 목록 반환
     * @param portfolioId
     * @return : List<PrAllInfoResp>
     */
    @GetMapping("/portfolio/{portfolio_id}/projects")
    public ResponseEntity<BasicResponse> getProjectInfo(@PathVariable(value = "portfolio_id") Long portfolioId){
        List<PrAllInfoResp> prAllInfoRespList = projectService.getProjectInfoList(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "프로젝트 정보 목록 반환", prAllInfoRespList));
    }


    /**
     * 프로젝트 수정
     * @param prUpdateReq
     * @return
     */
    @PutMapping("/project")
    public ResponseEntity<BasicResponse> updateProjectInfo(@RequestBody PrUpdateReq prUpdateReq){
        projectService.updateProject(prUpdateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "프로젝트가 수정되었습니다."));
    }


    /**
     * 프로젝트 삭제
     * @param prDeleteReq
     * @return
     */
    @DeleteMapping("/project")
    public ResponseEntity<BasicResponse> deleteProjectInfo(@RequestBody PrDeleteReq prDeleteReq){
        projectService.deleteProject(prDeleteReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "프로젝트가 삭제되었습니다."));
    }
}
