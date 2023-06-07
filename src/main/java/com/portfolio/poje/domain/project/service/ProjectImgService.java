package com.portfolio.poje.domain.project.service;

import com.portfolio.poje.domain.project.dto.PrImgDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProjectImgService {

    /**
     * 프로젝트 이미지 추가 및 삭제
     * @param projectId
     * @param prImgDelListReq
     * @param files
     * @throws IOException
     */
    void updateImages(Long projectId, PrImgDto.PrImgDelListReq prImgDelListReq, List<MultipartFile> files) throws IOException;
}
