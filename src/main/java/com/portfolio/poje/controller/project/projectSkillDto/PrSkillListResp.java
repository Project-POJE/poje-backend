package com.portfolio.poje.controller.project.projectSkillDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrSkillListResp {

    private String type;

    private List<PrSkillInfoResp> skills;

}
