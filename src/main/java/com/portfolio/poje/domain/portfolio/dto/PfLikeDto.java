package com.portfolio.poje.domain.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class PfLikeDto {

    /**
     * 포트폴리오 좋아요 정보 응답 Dto
     */
    @Getter
    @AllArgsConstructor
    public static class PfLikeInfoResp {

        private boolean likeStatus;

        private Long likeCount;
    }

}
