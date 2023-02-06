package com.portfolio.poje.controller.project.projectSkillDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor
public class PrSkillCreateReq {

    private Long projectId;

    @NotBlank(message = "기술을 선택해주세요.")
    private List<PrSkillListReq> skillSet;

}
