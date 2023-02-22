package com.portfolio.poje.domain.project.dto;

import lombok.AllArgsConstructor;
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

        private List<PrImgDelReq> prImgDelList;
    }


    /**
     * 프로젝트 이미지 삭제 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PrImgDelReq {

        private String prImgDelUrl;
    }

    /**
     * 프로젝트 이미지 응답 Dto
     */
    @Getter
    @AllArgsConstructor
    public static class PrImgInfoResp {

        private String imgUrl;    // 파일 저장 경로
    }


}
