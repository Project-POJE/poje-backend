package com.portfolio.poje.controller.project.projectSkillDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PrSkillCreateReq {

    private Long projectId;

    private List<PrSkillListReq> skillSet;

}
