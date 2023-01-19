package com.portfolio.poje.controller.member.memberDto;

import com.portfolio.poje.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoResponseDto {

    private String nickName;

    private String email;

    private String phoneNum;

    private String gender;

    private String academic;

    private String dept;

    private String birth;

    private String profileImg;

    private String intro;


    @Builder
    private MemberInfoResponseDto(Member member){
        this.nickName = member.getNickName();
        this.email = member.getEmail();
        this.phoneNum = member.getPhoneNum();
        this.gender = member.getGender();
        this.academic = member.getAcademic();
        this.dept = member.getDept();
        this.birth = member.getBirth();
        this.profileImg = member.getProfileImg();
        this.intro = member.getIntro();
    }

}
