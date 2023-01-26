package com.portfolio.poje.controller.member.memberDto;

import com.portfolio.poje.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class MemberJoinRequest {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank
    private String passwordConfirm;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickName;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "전화번호를 작성해주세요.")
    @Pattern(regexp = "[0-9]{10,11}", message = "숫자만 입력해주세요.")
    private String phoneNum;

    @NotBlank
    private String gender;

    @NotBlank
    private String birth;


    @Builder
    private MemberJoinRequest(String loginId, String password, String passwordConfirm, String nickName,
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

    public Member toEntity(){
        return Member.createMember()
                .loginId(loginId)
                .password(password)
                .nickName(nickName)
                .email(email)
                .phoneNum(phoneNum)
                .gender(gender)
                .birth(birth)
                .build();
    }

}
