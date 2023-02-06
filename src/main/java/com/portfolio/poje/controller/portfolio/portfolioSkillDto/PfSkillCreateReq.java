package com.portfolio.poje.controller.portfolio.portfolioSkillDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor
public class PfSkillCreateReq {

    @NotBlank(message = "기술을 선택해주세요.")
    private List<PfSkillListReq> skillSet;

}
