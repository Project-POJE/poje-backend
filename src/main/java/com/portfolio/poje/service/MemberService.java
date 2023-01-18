package com.portfolio.poje.service;

import com.portfolio.poje.config.jwt.JwtTokenProvider;
import com.portfolio.poje.config.jwt.TokenDto;
import com.portfolio.poje.controller.member.memberDto.MemberJoinRequestDto;
import com.portfolio.poje.controller.member.memberDto.MemberLoginRequestDto;
import com.portfolio.poje.controller.member.memberDto.TokenRequestDto;
import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.domain.member.RefreshToken;
import com.portfolio.poje.domain.member.RoleType;
import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.repository.MemberRepository;
import com.portfolio.poje.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    /**
     * 회원가입
     * @param memberJoinRequestDto
     */
    @Transactional
    public void join(MemberJoinRequestDto memberJoinRequestDto){
        Member member = Member.createMember()
                .loginId(memberJoinRequestDto.getLoginId())
                .password(passwordEncoder.encode(memberJoinRequestDto.getPassword()))
                .nickName(memberJoinRequestDto.getNickName())
                .email(memberJoinRequestDto.getEmail())
                .phoneNum(memberJoinRequestDto.getPhoneNum())
                .gender(memberJoinRequestDto.getGender())
                .birth(memberJoinRequestDto.getBirth())
                .role(RoleType.ROLE_USER)
                .build();

        memberRepository.save(member);
    }


    /**
     * 로그인 아이디 중복 확인
     * @param loginId
     * @return
     */
    @Transactional
    public boolean loginIdCheck(String loginId){
        return memberRepository.existsByLoginId(loginId);
    }


    /**
     * 로그인
     * @param loginDto
     * @return : TokenDto
     */
    @Transactional
    public TokenDto login(MemberLoginRequestDto loginDto){
        // 로그인 정보로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        RefreshToken refreshToken = RefreshToken.enrollRefreshToken(authentication.getName(), tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }


    /**
     * 로그아웃 시 DB에서 refresh token 제거
     * @param loginId
     */
    @Transactional
    public void deleteRefreshToken(String loginId){
        RefreshToken refreshToken = refreshTokenRepository.findByLoginId(loginId).orElseThrow(
                () -> new PojeException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );

        refreshTokenRepository.delete(refreshToken);
    }


    /**
     * access token 재발행
     * @param tokenRequestDto
     * @return
     */
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto){   // Filter에서 진행하는 방식 고민
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new PojeException(ErrorCode.REFRESH_TOKEN_NOT_VALIDATE);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // DB에서 Refresh Token 조회
        RefreshToken refreshToken = refreshTokenRepository.findByLoginId(authentication.getName()).orElseThrow(
                () -> new PojeException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );

        // 요청으로 받은 Refresh Token과 DB에서 조회한 Refresh Token이 일치하는지 검사
        if (!refreshToken.getRefreshToken().equals(tokenRequestDto.getRefreshToken())){
            throw new PojeException(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        // 새로운 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        // Refresh Token 정보 변경
        refreshToken.updateToken(tokenDto.getRefreshToken());

        return tokenDto;
    }

}