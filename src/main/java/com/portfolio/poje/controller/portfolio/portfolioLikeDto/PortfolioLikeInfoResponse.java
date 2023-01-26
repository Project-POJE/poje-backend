package com.portfolio.poje.controller.portfolio.portfolioLikeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioLikeInfoResponse {

    private boolean likeStatus;

    private Long likeCount;

}
