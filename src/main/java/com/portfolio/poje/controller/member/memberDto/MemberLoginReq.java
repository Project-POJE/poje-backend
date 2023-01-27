package com.portfolio.poje.controller.member.memberDto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberLoginReq {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;


    @Builder
    private MemberLoginReq(String loginId, String password){
        this.loginId = loginId;
        this.password = password;
    }

}
