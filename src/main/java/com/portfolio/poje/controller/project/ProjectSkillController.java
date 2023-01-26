package com.portfolio.poje.controller.project;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.project.projectSkillDto.ProjectSkillCreateRequest;
import com.portfolio.poje.service.project.ProjectSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class ProjectSkillController {

    private final ProjectSkillService projectSkillService;


    /**
     * 프로젝트에 사용한 기술 추가
     * @param projectSkillCreateRequest
     * @param projectId
     * @return
     */
    @PostMapping("/project/{project_id}/skill")
    public ResponseEntity<BasicResponse> createProjectSkill(@RequestBody ProjectSkillCreateRequest projectSkillCreateRequest,
                                                            @PathVariable(value = "project_id") Long projectId){
        projectSkillService.enroll(projectSkillCreateRequest, projectId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "추가되었습니다."));
    }


    /**
     * 프로젝트에 사용한 기술 삭제
     * @param projectId
     * @param skillId
     * @return
     */
    @DeleteMapping("/project/{project_id}/skill/{skill_id}")
    public ResponseEntity<BasicResponse> deleteProjectSkill(@PathVariable(value = "project_id") Long projectId,
                                                            @PathVariable(value = "skill_id") Long skillId){
        projectSkillService.deleteProjectSkill(projectId, skillId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "삭제되었습니다."));
    }

}
