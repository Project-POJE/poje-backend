package com.portfolio.poje.service.member;

import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.config.jwt.JwtTokenProvider;
import com.portfolio.poje.config.jwt.TokenDto;
import com.portfolio.poje.controller.member.memberDto.*;
import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.domain.member.RefreshToken;
import com.portfolio.poje.domain.member.RoleType;
import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.repository.member.MemberRepository;
import com.portfolio.poje.repository.member.RefreshTokenRepository;
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
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    /**
     * 회원가입
     * @param memberJoinRequest
     */
    @Transactional
    public void join(MemberJoinRequest memberJoinRequest){
        Member member = Member.createMember()
                .loginId(memberJoinRequest.getLoginId())
                .password(passwordEncoder.encode(memberJoinRequest.getPassword()))
                .nickName(memberJoinRequest.getNickName())
                .email(memberJoinRequest.getEmail())
                .phoneNum(memberJoinRequest.getPhoneNum())
                .gender(memberJoinRequest.getGender())
                .birth(memberJoinRequest.getBirth())
                .role(RoleType.ROLE_USER)
                .build();

        memberRepository.save(member);
    }


    /**
     * 로그인 아이디 중복 확인
     * @param loginId
     * @return : boolean
     */
    @Transactional(readOnly = true)
    public boolean loginIdCheck(String loginId){
        return memberRepository.existsByLoginId(loginId);
    }


    /**
     * 로그인
     * @param loginDto
     * @return : TokenDto
     */
    @Transactional
    public TokenDto login(MemberLoginRequest loginDto){
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
     * 사용자 정보 반환
     * @param loginId
     * @return : MemberInfoResponse
     */
    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(String loginId){
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        return MemberInfoResponse.builder()
                .member(member)
                .build();
    }


    /**
     * 사용자 정보 수정
     * @param memberUpdateRequest
     * @return : MemberInfoResponse
     */
    @Transactional
    public MemberInfoResponse updateMember(MemberUpdateRequest memberUpdateRequest){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        member.updateInfo(memberUpdateRequest.getNickName(), memberUpdateRequest.getEmail(),
                memberUpdateRequest.getPhoneNum(), memberUpdateRequest.getGender(),
                memberUpdateRequest.getAcademic(), memberUpdateRequest.getDept(),
                memberUpdateRequest.getBirth(), memberUpdateRequest.getProfileImg(),
                memberUpdateRequest.getIntro());

        return MemberInfoResponse.builder()
                .member(member)
                .build();
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
     * @param tokenRequest
     * @return : TokenDto
     */
    @Transactional
    public TokenDto reissue(TokenRequest tokenRequest){   // Filter에서 진행하는 방식 고민
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(tokenRequest.getRefreshToken())) {
            throw new PojeException(ErrorCode.REFRESH_TOKEN_NOT_VALIDATE);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequest.getAccessToken());

        // DB에서 Refresh Token 조회
        RefreshToken refreshToken = refreshTokenRepository.findByLoginId(authentication.getName()).orElseThrow(
                () -> new PojeException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );

        // 요청으로 받은 Refresh Token과 DB에서 조회한 Refresh Token이 일치하는지 검사
        if (!refreshToken.getRefreshToken().equals(tokenRequest.getRefreshToken())){
            throw new PojeException(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        // 새로운 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        // Refresh Token 정보 변경
        refreshToken.updateToken(tokenDto.getRefreshToken());

        return tokenDto;
    }

}