package com.portfolio.poje.controller.project.projectImgDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectImgInfoResponseDto {

    private String originalName;    // 파일 원본명

    private String filePath;    // 파일 저장 경로

    private Long fileSize;


    @Builder
    private ProjectImgInfoResponseDto(String originalName, String filePath, Long fileSize){
        this.originalName = originalName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

}
