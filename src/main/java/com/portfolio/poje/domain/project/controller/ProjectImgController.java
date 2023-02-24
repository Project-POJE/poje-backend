package com.portfolio.poje.domain.project.controller;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.domain.project.dto.PrImgDto;
import com.portfolio.poje.domain.project.service.ProjectImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/member")
@CrossOrigin("*")
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
     * @param prImgDelListReq
     * @param files
     * @return
     * @throws Exception
     */
    @PutMapping("/project/{project_id}/img")
    public ResponseEntity<BasicResponse> updateProjectImgList(@PathVariable(value = "project_id") Long projectId,
                                                              @RequestPart(value = "prImgDelList", required = false) PrImgDto.PrImgDelListReq prImgDelListReq,
                                                              @RequestPart(value = "projectImg", required = false) List<MultipartFile> files) throws Exception{
        projectImgService.updateImages(projectId, prImgDelListReq, files);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "수정되었습니다."));
    }


    /**
     * 프로젝트 이미지 목록 반환
     * @param projectId
     * @return : prImgList
     * @throws IOException
     */
    @GetMapping(value = "/project/{project_id}/img")
    public ResponseEntity<BasicResponse> getProjectImg(@PathVariable(value = "project_id") Long projectId){
        List<String> prImgList = projectImgService.getImgPath(projectId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "이미지 반환", prImgList));
    }


}
