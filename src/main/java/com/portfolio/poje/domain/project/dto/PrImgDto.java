package com.portfolio.poje.domain.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PrImgDto {

    /**
     * 프로젝트 이미지 삭제 목록 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PrImgDelListReq {

        private List<String> prImgDelList;
    }


}
