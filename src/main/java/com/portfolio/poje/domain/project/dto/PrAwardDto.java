package com.portfolio.poje.domain.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class PrAwardDto {

    /**
     * 포트폴리오 수상 정보 등록 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PrAwardCreateReq {

        @NotBlank(message = "주최를 입력해주세요.")
        private String supervision;

        @NotBlank(message = "순위를 입력해주세요. (e.g.3등 or 동상)")
        private String grade;

        @NotBlank(message = "설명을 입력해주세요.")
        private String description;

        @Builder
        private PrAwardCreateReq(String supervision, String grade, String description){
            this.supervision = supervision;
            this.grade = grade;
            this.description = description;
        }
    }


    /**
     * 포트폴리오 수상 정보 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PrAwardUpdateReq {

        private String supervision;

        private String grade;

        private String description;
    }


    /**
     * 포트폴리오 수상 정보 응답 Dto
     */
    @Getter
    public static class PrAwardInfoResp {

        private String supervision;

        private String grade;

        private String description;

        @Builder
        private PrAwardInfoResp(String supervision, String grade, String description){
            this.supervision = supervision;
            this.grade = grade;
            this.description = description;
        }
    }

}
