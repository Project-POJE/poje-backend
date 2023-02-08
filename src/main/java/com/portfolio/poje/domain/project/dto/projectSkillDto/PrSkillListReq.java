package com.portfolio.poje.domain.project.dto.projectSkillDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PrSkillListReq {

    private String type;

    private List<PrSkillInfoReq> skills;

}
