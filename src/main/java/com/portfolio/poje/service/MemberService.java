package com.portfolio.poje.service;

import com.portfolio.poje.config.jwt.JwtTokenProvider;
import com.portfolio.poje.config.jwt.TokenDto;
import com.portfolio.poje.controller.member.memberDto.MemberJoinRequestDto;
import com.portfolio.poje.controller.member.memberDto.MemberLoginRequestDto;
import com.portfolio.poje.controller.member.memberDto.TokenRequestDto;
import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.domain.member.RefreshToken;
import com.portfolio.poje.domain.member.RoleType;
import com.portfolio.poje.repository.MemberRepository;
import com.portfolio.poje.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
                .academic(memberJoinRequestDto.getAcademic())
                .dept(memberJoinRequestDto.getDept())
                .birth(memberJoinRequestDto.getBirth())
                .blogLink(memberJoinRequestDto.getBlogLink())
                .profileImg(memberJoinRequestDto.getProfileImg())
                .intro(memberJoinRequestDto.getIntro())
                .role(RoleType.ROLE_USER)
                .build();

        memberRepository.save(member);
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

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByLoginId(loginDto.getLoginId());
        RefreshToken newRefreshToken;

        if (refreshToken.isPresent()){
            newRefreshToken = refreshToken.get().updateToken(tokenDto.getRefreshToken());
        } else {
            newRefreshToken = RefreshToken.enrollRefreshToken(loginDto.getLoginId(), tokenDto.getRefreshToken());
        }

        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }


    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto){   // Filter에서 진행하는 방식 고민
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByLoginId(authentication.getName()).orElseThrow(
                () -> new RuntimeException("로그아웃 되었습니다.")
        );

        // Refresh Token 일치하는지 검사
        if (!refreshToken.getRefreshToken().equals(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("토큰의 사용자 정보가 일치하지 않습니다.");
        }

        // 새로운 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        // Refresh Token 정보 변경
        RefreshToken newRefreshToken = refreshToken.updateToken(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

}