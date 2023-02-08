package com.portfolio.poje.domain.project.dto.projectSkillDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PrSkillListResp {

    private String type;

    private List<PrSkillInfoResp> skills;

}
