package com.portfolio.poje.domain.ability.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

public class JobDto {

    /**
     * 직무 생성 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class JobCreateReq {

        @NotBlank(message = "생성할 직무 이름을 입력해주세요.")
        private String name;
    }


    /**
     * 직무 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class JobUpdateReq {

        @NotBlank(message = "수정할 직무 이름을 입력해주세요.")
        private String name;
    }


    /**
     * 직무 정보 응답 Dto
     */
    @Getter
    @AllArgsConstructor
    public static class JobInfoResp {

        private String name;
    }


    /**
     * 직무 정보 목록 응답 Dto
     */
    @Getter
    public static class JobListResp {
        private List<JobInfoResp> jobInfoRespList;

        public JobListResp(List<String> jobNameList){
            this.jobInfoRespList = jobNameList.stream()
                    .map(name -> new JobInfoResp(name))
                    .collect(Collectors.toList());
        }
    }

}
