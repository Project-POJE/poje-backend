package com.portfolio.poje.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PrSkillDto {

    /**
     * 프로젝트 기술 정보 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PrSkillInfoReq {

        private String name;
    }


    /**
     * 프로젝트 기술 타입 및 정보 목록 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PrSkillListReq {

        private String type;

        private List<PrSkillInfoReq> skills;
    }


    /**
     * 프로젝트 기술 정보 응답 Dto
     */
    @Getter
    @AllArgsConstructor
    public static class PrSkillInfoResp {

        private Long skillId;

        private String name;
    }


    /**
     * 프로젝트 기술 정보 목록 응답 Dto
     */
    @Getter
    @AllArgsConstructor
    public static class PrSkillListResp {

        private String type;

        private List<PrSkillInfoResp> skills;
    }

}
