package com.portfolio.poje.controller.member.memberDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.portfolio.poje.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberBasicInfoResponse {

    private String nickName;

    private String email;

    private String phoneNum;

    private String birth;

    private String academic;

    private String dept;

    private String profileImg;


    @Builder
    private MemberBasicInfoResponse(Member member){
        this.nickName = member.getNickName();
        this.email = member.getEmail();
        this.phoneNum = member.getPhoneNum();
        this.birth = member.getBirth();
        this.academic = member.getAcademic();
        this.dept = member.getDept();
        this.profileImg = member.getProfileImg();
    }

}
