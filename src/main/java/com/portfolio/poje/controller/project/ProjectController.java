package com.portfolio.poje.controller.project;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.project.projectDto.PrBasicInfoResp;
import com.portfolio.poje.controller.project.projectDto.PrUpdateReq;
import com.portfolio.poje.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping(value = "/member")
@RestController
public class ProjectController {

    private final ProjectService projectService;


    /**
     * 기본 프로젝트 생성
     * @param portfolioMap
     * @return
     */
    @PostMapping("/project")
    public ResponseEntity<BasicResponse> createBasicProject(@RequestBody Map<String, Long> portfolioMap){
        PrBasicInfoResp prBasicInfoResp = projectService.enrollBasicProject(portfolioMap.get("portfolioId"));

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "기본 프로젝트가 추가되었습니다", prBasicInfoResp));
    }


    /**
     * 프로젝트 수정
     * @param prUpdateReq
     * @param projectId
     * @return
     */
    @PutMapping("/portfolio/{portfolio_id}/project/{project_id}")
    public ResponseEntity<BasicResponse> updateProjectInfo(@RequestBody PrUpdateReq prUpdateReq,
                                                           @PathVariable(value = "project_id") Long projectId){
        projectService.updateProject(prUpdateReq, projectId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "프로젝트가 수정되었습니다."));
    }


    /**
     * 프로젝트 삭제
     * @param portfolioId
     * @param projectId
     * @return
     */
    @DeleteMapping("/portfolio/{portfolio_id}/project/{project_id}")
    public ResponseEntity<BasicResponse> deleteProjectInfo(@PathVariable(value = "portfolio_id") Long portfolioId,
                                                           @PathVariable(value = "project_id") Long projectId){
        projectService.deleteProject(portfolioId, projectId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "프로젝트가 삭제되었습니다."));
    }
}
