package com.portfolio.poje.controller.portfolio.portfolioSkillDto;

import com.portfolio.poje.domain.portfolio.PortfolioSkill;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PfSkillListResp {

    private String type;

    private String name;

    private String path;


    @Builder
    private PfSkillListResp(PortfolioSkill skill){
        this.type = skill.getType();
        this.name = skill.getName();
        this.path = skill.getPath();
    }

}
