package com.portfolio.poje.controller.project;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillCreateReq;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillDeleteReq;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillListResp;
import com.portfolio.poje.service.project.ProjectSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class ProjectSkillController {

    private final ProjectSkillService projectSkillService;


    /**
     * 프로젝트에 사용한 기술 추가
     * @param prSkillCreateReq
     * @return
     */
    @PostMapping("/project/skill")
    public ResponseEntity<BasicResponse> createProjectSkill(@RequestBody @Valid PrSkillCreateReq prSkillCreateReq){
        projectSkillService.enroll(prSkillCreateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "추가되었습니다."));
    }


//    /**
//     * 프로젝트 사용 기술 수정 (추가 or 삭제)
//     * @param projectId
//     * @param prSkillUpdateReq
//     * @return : List<PrSkillListResp>
//     */
//    @PutMapping("/project/{project_id}/skill")
//    public ResponseEntity<BasicResponse> updateProjectSkill(@PathVariable(value = "project_id") Long projectId,
//                                                            @RequestBody PrSkillUpdateReq prSkillUpdateReq){
//        projectSkillService.updateProjectSkill(projectId, prSkillUpdateReq.getSkillSet());
//
//        List<PrSkillListResp> prSkillListResp = projectSkillService.toPrSkillListDto(projectId);
//
//        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "수정되었습니다.", prSkillListResp));
//    }


    /**
     * 프로젝트 사용 기술 목록 반환
     * @param projectId
     * @return : List<PrSkillListResp>
     */
    @GetMapping("/project/{project_id}/skills")
    public ResponseEntity<BasicResponse> getProjectSkills(@PathVariable(value = "project_id") Long projectId){
        List<PrSkillListResp> prSkillListResp = projectSkillService.toPrSkillListDto(projectId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "프로젝트 기술 목록 반환", prSkillListResp));
    }


    /**
     * 프로젝트에 사용한 기술 삭제
     * @param prSkillDeleteReq
     * @return
     */
    @DeleteMapping("/project/skill")
    public ResponseEntity<BasicResponse> deleteProjectSkill(@RequestBody PrSkillDeleteReq prSkillDeleteReq){
        projectSkillService.deleteProjectSkill(prSkillDeleteReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "삭제되었습니다."));
    }

}
