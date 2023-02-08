package com.portfolio.poje.domain.member.dto.memberDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class MemberUpdateReq {

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
