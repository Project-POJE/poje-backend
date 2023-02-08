package com.portfolio.poje.domain.portfolio.dto.portfolioAwardDto;

import com.portfolio.poje.domain.portfolio.entity.PortfolioAward;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PfAwardInfoResp {

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
