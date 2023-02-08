package com.portfolio.poje.domain.portfolio.dto.portfolioLikeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PfLikeInfoResp {

    private boolean likeStatus;

    private Long likeCount;

}
