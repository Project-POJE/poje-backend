package com.portfolio.poje.controller.ability.licenseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class LicenseUpdateReq {

    private List<LicenseInfoReq> licenseList;

}
