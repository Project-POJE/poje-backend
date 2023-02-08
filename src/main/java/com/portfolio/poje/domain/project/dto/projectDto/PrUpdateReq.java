package com.portfolio.poje.domain.project.dto.projectDto;

import com.portfolio.poje.domain.project.dto.projectAwardDto.PrAwardUpdateReq;
import com.portfolio.poje.domain.project.dto.projectSkillDto.PrSkillListReq;
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
