package com.portfolio.poje.domain.portfolio.dto.portfolioDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PfUpdateReq {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "설명이나 소개를 입력해주세요.")
    private String description;

}
