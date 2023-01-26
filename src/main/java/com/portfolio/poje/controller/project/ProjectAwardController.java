package com.portfolio.poje.controller.project;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.project.projectAwardDto.ProjectAwardCreateRequest;
import com.portfolio.poje.controller.project.projectAwardDto.ProjectAwardUpdateRequest;
import com.portfolio.poje.service.project.ProjectAwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class ProjectAwardController {

    private final ProjectAwardService projectAwardService;


    /**
     * 프로젝트 수상 정보 등록
     * @param projectAwardCreateRequest
     * @return
     */
    @PostMapping("/project/{project_id}/award")
    public ResponseEntity<BasicResponse> createProjectAward(@RequestBody ProjectAwardCreateRequest projectAwardCreateRequest,
                                                            @PathVariable(value = "project_id") Long projectId){
        projectAwardService.enroll(projectAwardCreateRequest, projectId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "등록되었습니다."));
    }


    /**
     * 프로젝트 수상 정보 수정
     * @param projectAwardUpdateRequest
     * @param projectId
     * @return
     */
    @PutMapping("/project/{project_id}/award")
    public ResponseEntity<BasicResponse> updateProjectAward(@RequestBody ProjectAwardUpdateRequest projectAwardUpdateRequest,
                                                            @PathVariable(value = "project_id") Long projectId){
        projectAwardService.updateAwardInfo(projectAwardUpdateRequest, projectId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "수정되었습니다."));
    }


    /**
     * 프로젝트 수상 정보 삭제
     * @param projectId
     * @return
     */
    @DeleteMapping("/project/{project_id}/award")
    public ResponseEntity<BasicResponse> deleteProjectAwardInfo(@PathVariable(value = "project_id") Long projectId){
        projectAwardService.deleteAward(projectId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "삭제되었습니다."));
    }

}
