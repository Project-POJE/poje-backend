package com.portfolio.poje.domain.portfolio.dto.portfolioDto;

import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import lombok.Builder;
import lombok.Getter;

@Getter
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