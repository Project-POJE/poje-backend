package com.portfolio.poje.controller.project.projectDto;

import com.portfolio.poje.controller.project.projectAwardDto.PrAwardUpdateReq;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillListReq;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PrUpdateReq {

    private PrInfoReq prInfo;

    private PrAwardUpdateReq prAwardInfo;

    private List<PrSkillListReq> skillSet;

}
