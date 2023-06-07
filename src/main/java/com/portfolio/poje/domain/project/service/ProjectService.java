package com.portfolio.poje.domain.project.service;

import com.portfolio.poje.domain.project.dto.PrDto;
import com.portfolio.poje.domain.project.dto.PrImgDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProjectService {

    /**
     * 기본 프로젝트 생성
     * @param portfolioId
     * @return PrAllInfoResp
     */
    PrDto.PrAllInfoResp enrollBasicProject(Long portfolioId);


    /**
     * 포트폴리오의 프로젝트별 관련 정보 목록 반환
     * @param portfolioId
     * @return List<PrAllInfoResp>
     */
    List<PrDto.PrAllInfoResp> getProjectInfoList(Long portfolioId);


    /**
     * 프로젝트 수정
     * @param projectId
     * @param prUpdateReq
     * @param prImgDelListReq
     * @param files
     * @return PrAllInforesp
     * @throws IOException
     */
    PrDto.PrAllInfoResp updateProject(Long projectId,
                                      PrDto.PrUpdateReq prUpdateReq,
                                      PrImgDto.PrImgDelListReq prImgDelListReq,
                                      List<MultipartFile> files) throws IOException;


    /**
     * 프로젝트 삭제
     * @param projectId
     */
    void deleteProject(Long projectId);
}
