package com.portfolio.poje.domain.ability.dto.licenseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class LicenseUpdateReq {

    private List<LicenseInfoReq> licenseList;

}
