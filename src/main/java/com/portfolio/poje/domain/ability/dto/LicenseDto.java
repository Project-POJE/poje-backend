package com.portfolio.poje.domain.ability.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class LicenseDto {

    /**
     * 자격증 등록 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LicenseCreateReq {

        @NotBlank(message = "자격증 명을 입력해주세요.")
        private String name;
    }


    /**
     * 자격증 목록으로 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LicenseUpdateReq {

        private List<LicenseInfoReq> licenseList;
    }


    /**
     * 자격증 정보 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LicenseInfoReq {

        private String name;
    }


    /**
     * 자격증 정보 응답 Dto
     */
    @Getter
    @AllArgsConstructor
    public static class LicenseInfoResp {

        private Long licenseId;
        private String name;
    }

}
