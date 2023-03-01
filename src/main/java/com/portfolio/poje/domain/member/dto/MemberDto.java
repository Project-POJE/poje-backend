package com.portfolio.poje.domain.member.dto;

import com.portfolio.poje.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MemberDto {

    /**
     * 회원가입 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class MemberJoinReq {

        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        private String loginId;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
                message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
        private String password;

        @NotBlank(message = "비밀번호를 다시 입력해주세요.")
        private String passwordConfirm;

        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String nickName;

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @Pattern(regexp = "[0-9]{10,11}", message = "숫자만 입력해주세요.")
        private String phoneNum;    // 입력 선택

        @NotBlank(message = "성별을 선택해주세요.")
        private String gender;

        @NotBlank(message = "생년월일을 입력해주세요.")
        private String birth;

        @Builder
        private MemberJoinReq(String loginId, String password, String passwordConfirm, String nickName,
                              String email, String phoneNum, String gender, String birth){
            this.loginId = loginId;
            this.password = password;
            this.passwordConfirm = passwordConfirm;
            this.nickName = nickName;
            this.email = email;
            this.phoneNum = phoneNum;
            this.gender = gender;
            this.birth = birth;
        }
    }


    /**
     * 로그인 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class MemberLoginReq {

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


    /**
     * 회원 정보 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class MemberUpdateReq {

        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String nickName;

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @Pattern(regexp = "[0-9]{10,11}", message = "숫자만 입력해주세요.")
        private String phoneNum;

        @NotBlank(message = "성별을 선택해주세요.")
        private String gender;

        @NotBlank(message = "생년월일을 입력해주세요.")
        private String birth;

        private String academic;

        private String dept;

        private String gitHubLink;

        private String blogLink;
    }


    @Getter
    @NoArgsConstructor
    public static class PasswordUpdateReq {

        private String existPassword;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
                message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
        private String newPassword;

        private String newPasswordConfirm;


    }


    /**
     * 회원 정보 응답 Dto
     */
    @Getter
    public static class MemberInfoResp {

        private String nickName;

        private String email;

        private String phoneNum;

        private String gender;

        private String academic;

        private String dept;

        private String birth;

        private String profileImg;

        private String gitHubLink;

        private String blogLink;

        @Builder
        private MemberInfoResp(Member member){
            this.nickName = member.getNickName();
            this.email = member.getEmail();
            this.phoneNum = member.getPhoneNum();
            this.gender = member.getGender();
            this.academic = member.getAcademic();
            this.dept = member.getDept();
            this.birth = member.getBirth();
            this.profileImg = member.getProfileImg();
            this.gitHubLink = member.getGitHubLink();
            this.blogLink = member.getBlogLink();
        }
    }

}
