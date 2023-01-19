package com.portfolio.poje.controller.member.memberDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequestDto {

    private String nickName;

    private String email;

    private String phoneNum;

    private String gender;

    private String academic;

    private String dept;

    private String birth;

    private String profileImg;

    private String intro;

}
