package com.portfolio.poje.controller.project.projectSkillDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PrSkillListReq {

    private String type;

    private List<PrSkillInfoReq> skills;

}
