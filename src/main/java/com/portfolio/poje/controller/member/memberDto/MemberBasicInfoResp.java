package com.portfolio.poje.controller.member.memberDto;

import com.portfolio.poje.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberBasicInfoResp {

    private String nickName;

    private String email;

    private String phoneNum;

    private String birth;

    private String academic;

    private String dept;

    private String profileImg;


    @Builder
    private MemberBasicInfoResp(Member member){
        this.nickName = member.getNickName();
        this.email = member.getEmail();
        this.phoneNum = member.getPhoneNum();
        this.birth = member.getBirth();
        this.academic = member.getAcademic();
        this.dept = member.getDept();
        this.profileImg = member.getProfileImg();
    }

}
