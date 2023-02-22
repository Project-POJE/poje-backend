package com.portfolio.poje.domain.portfolio.dto;

import com.portfolio.poje.domain.portfolio.entity.PortfolioAward;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class PfAwardDto {

    /**
     * 포트폴리오 수상 정보 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PfAwardUpdateReq {

        @NotBlank(message = "주최을 입력해주세요.")
        private String supervision;

        @NotBlank(message = "순위를 입력해주세요. (e.g.3등 or 동상)")
        private String grade;

        @NotBlank(message = "설명을 입력해주세요.")
        private String description;
    }


    /**
     * 포트폴리오 수상 정보 응답 Dto
     */
    @Getter
    public static class PfAwardInfoResp {

        private Long portfolioAwardId;

        private String supervision;

        private String grade;

        private String description;

        @Builder
        private PfAwardInfoResp(PortfolioAward portfolioAward){
            this.portfolioAwardId = portfolioAward.getId();
            this.supervision = portfolioAward.getSupervision();
            this.grade = portfolioAward.getGrade();
            this.description = portfolioAward.getDescription();
        }
    }

}
