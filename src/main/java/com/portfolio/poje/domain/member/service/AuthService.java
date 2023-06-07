package com.portfolio.poje.domain.member.service;

import com.portfolio.poje.config.jwt.TokenDto;
import com.portfolio.poje.domain.member.dto.MemberDto;

public interface AuthService {

    /**
     * 회원가입
     * @param memberJoinReq
     */
    void join(MemberDto.MemberJoinReq memberJoinReq);


    /**
     * 로그인
     * @param loginReq
     * @return TokenDto
     */
    TokenDto login(MemberDto.MemberLoginReq loginReq);


    /**
     * 로그아웃
     * @param accessToken
     */
    void logout(String accessToken);


    /**
     * 토큰 재발급
     * @param accessToken
     * @param refreshToken
     * @return TokenDto
     */
    TokenDto reissue(String accessToken, String refreshToken);
}
