package com.portfolio.poje.controller.portfolio.portfolioDto;

import com.portfolio.poje.domain.portfolio.Portfolio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PfInfoResp {

    private Long portfolioId;

    private String title;

    private String description;

    private String backgroundImg;

    private String jobName;


    @Builder
    private PfInfoResp(Portfolio portfolio){
        this.portfolioId = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.backgroundImg = portfolio.getBackgroundImg();
        this.jobName = portfolio.getJob().getName();
    }

}
