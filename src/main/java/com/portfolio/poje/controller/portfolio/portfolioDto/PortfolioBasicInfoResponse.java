package com.portfolio.poje.controller.portfolio.portfolioDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.portfolio.poje.controller.member.memberDto.MemberBasicInfoResponse;
import com.portfolio.poje.domain.portfolio.Portfolio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortfolioBasicInfoResponse {

    private Long portfolioId;

    private String backgroundImg;

    private MemberBasicInfoResponse memberBasicInfoResponse;


    @Builder
    private PortfolioBasicInfoResponse(Portfolio portfolio){
        this.portfolioId = portfolio.getId();
        this.backgroundImg = portfolio.getBackgroundImg();
        this.memberBasicInfoResponse = MemberBasicInfoResponse.builder()
                .member(portfolio.getWriter())
                .build();
    }

}
