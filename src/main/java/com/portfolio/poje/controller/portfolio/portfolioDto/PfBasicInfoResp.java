package com.portfolio.poje.controller.portfolio.portfolioDto;

import com.portfolio.poje.controller.member.memberDto.MemberBasicInfoResp;
import com.portfolio.poje.domain.portfolio.Portfolio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PfBasicInfoResp {

    private Long portfolioId;

    private String backgroundImg;

    private MemberBasicInfoResp memberBasicInfoResp;


    @Builder
    private PfBasicInfoResp(Portfolio portfolio){
        this.portfolioId = portfolio.getId();
        this.backgroundImg = portfolio.getBackgroundImg();
        this.memberBasicInfoResp = MemberBasicInfoResp.builder()
                .member(portfolio.getWriter())
                .build();
    }

}
