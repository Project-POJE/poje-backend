package com.portfolio.poje.domain.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PfSkillDto {

    /**
     * 포트폴리오 기술 정보 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PfSkillInfoReq {

        private String name;

        private String path;
    }


    /**
     * 포트폴리오 기술 타입 및 정보 목록 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PfSkillListReq {

        private String type;

        private List<PfSkillInfoReq> skills;
    }


    /**
     * 포트폴리오 기술 목록 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PfSkillUpdateReq {

        private List<PfSkillListReq> skillSet;
    }


    /**
     * 포트폴리오 기술 등록 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PfSkillCreateReq {

        private List<PfSkillListReq> skillSet;
    }


    /**
     * 포트폴리오 기술 정보 응답 Dto
     */
    @Getter
    @AllArgsConstructor
    public static class PfSkillInfoResp {

        private Long skillId;

        private String name;

        private String path;
    }


    /**
     * 포트폴리오 기술 정보 목록 응답 Dto
     */
    @Getter
    @AllArgsConstructor
    public static class PfSkillListResp {

        private String type;

        private List<PfSkillInfoResp> skills;
    }

}
