package com.portfolio.poje.domain.project.service;

import com.portfolio.poje.domain.project.dto.PrSkillDto;

import java.util.List;

public interface ProjectSkillService {

    /**
     * 프로젝트 사용 기술 수정 (추가 or 삭제)
     * @param projectId
     * @param skillList
     */
    void updateProjectSkill(Long projectId, List<PrSkillDto.PrSkillListReq> skillList);


    /**
     * 프로젝트 사용 기술 타입별 정렬
     * @param projectId
     * @return List<PrSkillListResp>
     */
    List<PrSkillDto.PrSkillListResp> toPrSkillListDto(Long projectId);
}
