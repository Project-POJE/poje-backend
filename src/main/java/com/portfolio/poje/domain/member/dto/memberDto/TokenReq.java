package com.portfolio.poje.domain.member.dto.memberDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class TokenReq {

    @NotBlank(message = "access token을 입력해주세요.")
    private String accessToken;

    @NotBlank(message = "refresh token을 입력해주세요.")
    private String refreshToken;

}
