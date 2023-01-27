package com.portfolio.poje.controller.ability.licenseDto;

import com.portfolio.poje.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class LicenseListResp {

    private List<LicenseInfoResp> licenseName;

    public LicenseListResp(Member member){
        this.licenseName = member.getLicenseList().stream()
                .map(l -> new LicenseInfoResp(l.getName()))
                .collect(Collectors.toList());
    }


}
