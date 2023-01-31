package com.portfolio.poje.controller.project.projectImgDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PrImgInfoResp {

    private String originalName;    // 파일 원본명

    private String filePath;    // 파일 저장 경로


    @Builder
    private PrImgInfoResp(String originalName, String filePath){
        this.originalName = originalName;
        this.filePath = filePath;
    }

}
