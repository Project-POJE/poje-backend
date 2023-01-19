package com.portfolio.poje.controller.ability.licenseDto;

import com.portfolio.poje.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class LicenseListResponseDto {

    private List<LicenseInfoResponseDto> licenseName;

    public LicenseListResponseDto(Member member){
        this.licenseName = member.getLicenseList().stream()
                .map(l -> new LicenseInfoResponseDto(l.getName()))
                .collect(Collectors.toList());
    }


}
