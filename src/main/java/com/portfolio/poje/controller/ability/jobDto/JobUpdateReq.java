package com.portfolio.poje.controller.ability.jobDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class JobUpdateReq {

    @NotBlank(message = "수정할 직무 이름을 입력해주세요.")
    private String name;

}
