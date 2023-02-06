package com.portfolio.poje.controller.project.projectAwardDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PrAwardCreateReq {

    private Long projectId;

    @NotBlank(message = "주최를 입력해주세요.")
    private String supervision;

    @NotBlank(message = "순위를 입력해주세요. (e.g.3등 or 동상)")
    private String grade;

    @NotBlank(message = "설명을 입력해주세요.")
    private String description;


    @Builder
    private PrAwardCreateReq(String supervision, String grade, String description){
        this.supervision = supervision;
        this.grade = grade;
        this.description = description;
    }
}
