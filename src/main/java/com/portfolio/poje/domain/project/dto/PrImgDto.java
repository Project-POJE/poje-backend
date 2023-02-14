package com.portfolio.poje.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class PrImgDto {

    /**
     * 프로젝트 이미지 응답 Dto
     */
    @Getter
    @AllArgsConstructor
    public static class PrImgInfoResp {

        private String imgUrl;    // 파일 저장 경로
    }


}
