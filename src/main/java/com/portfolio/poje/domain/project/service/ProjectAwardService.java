package com.portfolio.poje.domain.project.service;

import com.portfolio.poje.domain.project.dto.PrAwardDto;

public interface ProjectAwardService {

    /**
     * 프로젝트 수상 정보 수정
     * @param projectId
     * @param prAwardUpdateReq
     */
    void updateAwardInfo(Long projectId, PrAwardDto.PrAwardUpdateReq prAwardUpdateReq);
}
