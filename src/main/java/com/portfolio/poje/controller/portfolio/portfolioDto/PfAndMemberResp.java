package com.portfolio.poje.controller.portfolio.portfolioDto;

import com.portfolio.poje.domain.portfolio.Portfolio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PfAndMemberResp {

    // Portfolio
    private Long portfolioId;

    private String title;

    private String description;

    private String backgroundImg;

    // Member
    private String nickName;

    private String profileImg;

    // PortfolioLike
    private int likeCount;


    @Builder
    private PfAndMemberResp(Portfolio portfolio){
        this.portfolioId = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.backgroundImg = portfolio.getBackgroundImg();

        this.nickName = portfolio.getWriter().getNickName();
        this.profileImg = portfolio.getWriter().getProfileImg();

        this.likeCount = portfolio.getLikes().size();
    }

}
