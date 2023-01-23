package com.portfolio.poje.controller.project;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.project.projectDto.ProjectCreateRequestDto;
import com.portfolio.poje.controller.project.projectDto.ProjectUpdateRequestDto;
import com.portfolio.poje.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/member")
@RestController
public class ProjectController {

    private final ProjectService projectService;


    /**
     * 새로운 프로젝트 생성
     * @param projectCreateRequestDto
     * @param files
     * @param portfolioId
     * @return
     * @throws IOException
     */
    @PostMapping("/portfolio/{portfolio_id}/project")
    public ResponseEntity<BasicResponse> createProject(@RequestBody ProjectCreateRequestDto projectCreateRequestDto,
                                                       @RequestPart(value="image", required=false) List<MultipartFile> files,
                                                       @PathVariable(value = "portfolio_id") Long portfolioId) throws Exception {
        projectService.enroll(projectCreateRequestDto, files, portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "새로운 프로젝트가 생성되었습니다."));
    }


    /**
     * 프로젝트 수정
     * @param projectUpdateRequestDto
     * @param projectId
     * @return
     */
    @PutMapping("/portfolio/{portfolio_id}/project/{project_id}")
    public ResponseEntity<BasicResponse> updateProjectInfo(@RequestBody ProjectUpdateRequestDto projectUpdateRequestDto,
                                                           @PathVariable(value = "project_id") Long projectId){
        projectService.updateProject(projectUpdateRequestDto, projectId);

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
