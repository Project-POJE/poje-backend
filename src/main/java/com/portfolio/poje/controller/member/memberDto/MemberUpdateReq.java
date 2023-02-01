package com.portfolio.poje.controller.member.memberDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateReq {

    private String nickName;

    private String email;

    private String phoneNum;

    private String gender;

    private String academic;

    private String dept;

    private String birth;

    private String gitHubLink;

    private String blogLink;

}
