package com.portfolio.poje.domain.ability.dto.jobDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class JobCreateReq {

    @NotBlank(message = "생성할 직무 이름을 입력해주세요.")
    private String name;

}
