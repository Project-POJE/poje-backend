package com.portfolio.poje.controller.ability.licenseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LicenseCreateReq {

    @NotBlank(message = "자격증 명을 입력해주세요.")
    private String name;

}
