package com.portfolio.poje.controller.project;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.project.projectAwardDto.ProjectAwardCreateRequestDto;
import com.portfolio.poje.controller.project.projectAwardDto.ProjectAwardInfoResponseDto;
import com.portfolio.poje.controller.project.projectAwardDto.ProjectAwardUpdateRequestDto;
import com.portfolio.poje.service.ProjectAwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProjectAwardController {

    private final ProjectAwardService projectAwardService;


    /**
     * 프로젝트 수상 정보 등록
     * @param projectAwardCreateRequestDto
     * @return
     */
    @PostMapping("/member/project/{project_id}/award")
    public ResponseEntity<BasicResponse> createProjectAward(ProjectAwardCreateRequestDto projectAwardCreateRequestDto,
                                                            @PathVariable(value = "project_id") Long projectId){
        projectAwardService.enroll(projectAwardCreateRequestDto, projectId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "등록되었습니다."));
    }


    /**
     * 프로젝트 수상 정보 수정
     * @param projectAwardUpdateRequestDto
     * @param projectId
     * @param awardId
     * @return
     */
    @PutMapping("/member/project/{project_id}/award/{award_id}")
    public ResponseEntity<BasicResponse> updateProjectAward(ProjectAwardUpdateRequestDto projectAwardUpdateRequestDto,
                                                            @PathVariable(value = "project_id") Long projectId,
                                                            @PathVariable(value = "award_id") Long awardId){
        projectAwardService.updateAwardInfo(projectAwardUpdateRequestDto, awardId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "수정되었습니다."));
    }


    /**
     * 프로젝트 수상 정보 반환
     * @param projectId
     * @return : ProjectAwardInfoResponseDto
     */
    @GetMapping("/member/project/{project_id}/award")
    public ResponseEntity<BasicResponse> getProjectAwardInfo(@PathVariable(value = "project_id") Long projectId){
        ProjectAwardInfoResponseDto projectAwardInfoResponseDto = projectAwardService.getAwardList(projectId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "프로젝트 수상 정보 반환", projectAwardInfoResponseDto));
    }


    /**
     * 프로젝트 수상 정보 삭제
     * @param projectId
     * @param awardId
     * @return
     */
    @DeleteMapping("/member/project/{project_id}/award/{award_id}")
    public ResponseEntity<BasicResponse> deleteProjectAwardInfo(@PathVariable(value = "project_id") Long projectId,
                                                                @PathVariable(value = "award_id") Long awardId){
        projectAwardService.deleteAward(projectId, awardId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "삭제되었습니다."));
    }

}
