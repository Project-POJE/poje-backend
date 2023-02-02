package com.portfolio.poje.controller.portfolio.portfolioSkillDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class PfSkillListResp {

    private String type;

    private List<PfSkillInfoResp> skills;

}
