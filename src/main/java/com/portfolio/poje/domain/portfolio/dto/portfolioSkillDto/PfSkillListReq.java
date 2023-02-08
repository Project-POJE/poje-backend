package com.portfolio.poje.domain.portfolio.dto.portfolioSkillDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PfSkillListReq {

    private String type;

    private List<PfSkillInfoReq> skills;

}
