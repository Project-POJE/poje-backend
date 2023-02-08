package com.portfolio.poje.domain.portfolio.dto.portfolioAwardDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PfAwardUpdateReq {

    private Long portfolioAwardId;

    @NotBlank(message = "주최을 입력해주세요.")
    private String supervision;

    @NotBlank(message = "순위를 입력해주세요. (e.g.3등 or 동상)")
    private String grade;

    @NotBlank(message = "설명을 입력해주세요.")
    private String description;

}
