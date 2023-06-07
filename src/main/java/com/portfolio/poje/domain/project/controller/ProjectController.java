package com.portfolio.poje.domain.project.controller;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.domain.project.dto.PrDto;
import com.portfolio.poje.domain.project.dto.PrImgDto;
import com.portfolio.poje.domain.project.service.serviceImpl.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/member")
@RestController
public class ProjectController {

    private final ProjectServiceImpl projectServiceImpl;


    /**
     * 기본 프로젝트 생성
     * @param portfolioId
     * @return : PrAllInfoResp
     */
    @PostMapping("/portfolio/{portfolio_id}/project")
    public ResponseEntity<BasicResponse> createBasicProject(@PathVariable(value = "portfolio_id") Long portfolioId){
        PrDto.PrAllInfoResp prAllInfoResp = projectServiceImpl.enrollBasicProject(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "기본 프로젝트가 추가되었습니다", prAllInfoResp));
    }


    /**
     * 프로젝트 정보 목록 반환
     * @param portfolioId
     * @return : List<PrAllInfoResp>
     */
    @GetMapping("/portfolio/{portfolio_id}/projects")
    public ResponseEntity<BasicResponse> getProjectInfo(@PathVariable(value = "portfolio_id") Long portfolioId){
        List<PrDto.PrAllInfoResp> prAllInfoRespList = projectServiceImpl.getProjectInfoList(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "프로젝트 정보 목록 반환", prAllInfoRespList));
    }


    /**
     * 프로젝트 수정
     * @param projectId
     * @param prUpdateReq
     * @param prImgDelListReq
     * @param files
     * @return : prAllInfoResp
     * @throws Exception
     */
    @PutMapping("/project/{project_id}")
    public ResponseEntity<BasicResponse> updateProjectInfo(@PathVariable(value = "project_id") Long projectId,
                                                           @RequestPart(value = "projectUpdateReq") PrDto.PrUpdateReq prUpdateReq,
                                                           @RequestPart(value = "prImgDelList", required = false) PrImgDto.PrImgDelListReq prImgDelListReq,
                                                           @RequestPart(value = "projectImg", required = false)List<MultipartFile> files) throws Exception{
        PrDto.PrAllInfoResp prAllInfoResp = projectServiceImpl.updateProject(projectId, prUpdateReq, prImgDelListReq, files);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "프로젝트가 수정되었습니다.", prAllInfoResp));
    }


    /**
     * 프로젝트 삭제
     * @param projectId
     * @return
     */
    @DeleteMapping("/project/{project_id}")
    public ResponseEntity<BasicResponse> deleteProjectInfo(@PathVariable(value = "project_id") Long projectId){
        projectServiceImpl.deleteProject(projectId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "프로젝트가 삭제되었습니다."));
    }
}
