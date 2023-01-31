package com.portfolio.poje.controller.project;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.service.project.ProjectImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class ProjectImgController {

    private final ProjectImgService projectImgService;


    /**
     * 프로젝트 이미지 업로드
     * @param projectId
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/project/{project_id}/img")
    public ResponseEntity<BasicResponse> enroll(@PathVariable(value = "project_id") Long projectId,
                                                @RequestPart(value = "projectImg") List<MultipartFile> files) throws Exception{
        projectImgService.enrollImages(projectId, files);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "등록되었습니다."));
    }


    /**
     * 프로젝트 이미지 추가 및 삭제
     * @param projectId
     * @param files
     * @return
     * @throws Exception
     */
    @PutMapping("/project/{project_id}/img")
    public ResponseEntity<BasicResponse> updateProjectImgList(@PathVariable(value = "project_id") Long projectId,
                                                              @RequestPart(value = "projectImg", required = false) List<MultipartFile> files) throws Exception{
        projectImgService.updateImages(projectId, files);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "수정되었습니다."));
    }


}
