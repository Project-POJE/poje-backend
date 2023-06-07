package com.portfolio.poje.domain.member.service;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.jwt.JwtTokenProvider;
import com.portfolio.poje.config.jwt.TokenDto;
import com.portfolio.poje.domain.member.dto.MemberDto;
import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.member.entity.RoleType;
import com.portfolio.poje.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.portfolio.poje.config.aws.DefaultImage.DEFAULT_PROFILE_IMG;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.live.rtk}")
    private long refreshTokenExpiresIn;


    /**
     * 회원가입
     * @param memberJoinReq
     */
    @Override
    public void join(MemberDto.MemberJoinReq memberJoinReq) {
        memberRepository.findByLoginId(memberJoinReq.getLoginId()).ifPresent(a -> {
            throw new PojeException(ErrorCode.ID_ALREADY_EXIST);
        });

        Member member = Member.createMember()
                .loginId(memberJoinReq.getLoginId())
                .password(passwordEncoder.encode(memberJoinReq.getPassword()))
                .nickName(memberJoinReq.getNickName())
                .email(memberJoinReq.getEmail())
                .phoneNum(memberJoinReq.getPhoneNum())
                .gender(memberJoinReq.getGender())
                .birth(memberJoinReq.getBirth())
                .profileImg(DEFAULT_PROFILE_IMG)
                .role(RoleType.ROLE_USER)
                .build();

        memberRepository.save(member);
    }


    /**
     * 로그인
     * @param loginDto
     * @return TokenDto
     */
    @Override
    public TokenDto login(MemberDto.MemberLoginReq loginDto){
        // 로그인 정보로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        // Redis에 저장 - 만료 시간 설정을 통해 자동 삭제 처리
        redisTemplate.opsForValue().set(
                authentication.getName(),
                tokenDto.getRefreshToken(),
                refreshTokenExpiresIn,
                TimeUnit.MILLISECONDS
        );

        return tokenDto;
    }


    /**
     * 로그아웃 시 유효한 access token을 redis 블랙리스트로 추가
     * @param accessToken
     */
    @Override
    public void logout(String accessToken){
        if (!jwtTokenProvider.validateToken(accessToken)){
            throw new PojeException(ErrorCode.ACCESS_TOKEN_NOT_VALIDATE);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // 레디스에 인증 정보로 저장된 refresh token이 존재하는지 확인
        if (redisTemplate.opsForValue().get(authentication.getName()) != null){
            // 리프레시 토큰 삭제
            redisTemplate.delete(authentication.getName());
        }

        // access token 유효시간으로 Black list에 저장
        Long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }


    /**
     * access token 재발행
     * @param requestAccessToken
     * @param requestRefreshToken
     * @return TokenDto
     */
    @Override
    public TokenDto reissue(String requestAccessToken, String requestRefreshToken){   // Filter에서 진행하는 방식 고민
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(requestRefreshToken)) {
            throw new PojeException(ErrorCode.REFRESH_TOKEN_NOT_VALIDATE);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(requestAccessToken);

        // Redis에 저장된 Refresh Token 정보 추출
        String refreshToken = redisTemplate.opsForValue().get(authentication.getName());

        // 요청으로 받은 Refresh Token과 DB에서 조회한 Refresh Token이 일치하는지 검사
        if (!refreshToken.equals(requestRefreshToken)){
            throw new PojeException(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        // 새로운 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        redisTemplate.opsForValue().set(
                authentication.getName(),
                tokenDto.getRefreshToken(),
                refreshTokenExpiresIn,
                TimeUnit.MILLISECONDS
        );

        return tokenDto;
    }


}
