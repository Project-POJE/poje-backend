package com.portfolio.poje.domain.member.service;

import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.config.aws.S3FileUploader;
import com.portfolio.poje.config.jwt.JwtTokenProvider;
import com.portfolio.poje.config.jwt.TokenDto;
import com.portfolio.poje.domain.member.dto.MemberDto;
import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.member.entity.RoleType;
import com.portfolio.poje.domain.member.repository.MemberRepository;
import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

import static com.portfolio.poje.config.aws.DefaultImage.DEFAULT_PROFILE_IMG;


@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final S3FileUploader fileUploader;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${jwt.live.rtk}")
    private long refreshTokenExpiresIn;

    /**
     * 회원가입
     * @param memberJoinReq
     */
    @Transactional
    public void join(MemberDto.MemberJoinReq memberJoinReq){
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
     * 사용자 정보 반환
     * @param loginId
     * @return : MemberInfoResp
     */
    @Transactional(readOnly = true)
    public MemberDto.MemberInfoResp getMemberInfo(String loginId){
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        return MemberDto.MemberInfoResp.builder()
                .member(member)
                .build();
    }


    /**
     * 사용자 정보 수정
     * @param memberUpdateReq
     * @return : MemberInfoResp
     */
    @Transactional
    public MemberDto.MemberInfoResp updateMember(MemberDto.MemberUpdateReq memberUpdateReq, MultipartFile file) throws Exception{
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        if (member.getProfileImg().equals(DEFAULT_PROFILE_IMG) && file != null) {     // 기본 이미지 && 전달받은 이미지 o
            member.updateProfileImg(fileUploader.uploadFile(file, "profile"));          // 전달받은 이미지로 변경

        } else if (!member.getProfileImg().equals(DEFAULT_PROFILE_IMG) && file != null){ // 업로드 된 이미지 && 전달받은 이미지 o
            fileUploader.deleteFile(member.getProfileImg(), "profile");             // 이미지 삭제 후 전달받은 이미지로 변경
            member.updateProfileImg(fileUploader.uploadFile(file, "profile"));
        }

        member.updateInfo(memberUpdateReq.getNickName(), memberUpdateReq.getEmail(),
                          memberUpdateReq.getPhoneNum(), memberUpdateReq.getGender(),
                          memberUpdateReq.getAcademic(), memberUpdateReq.getDept(),
                          memberUpdateReq.getBirth(), memberUpdateReq.getGitHubLink(),
                          memberUpdateReq.getBlogLink());

        return MemberDto.MemberInfoResp.builder()
                .member(member)
                .build();
    }


    /**
     * 비밀번호 변경
     * @param passwordUpdateReq
     */
    @Transactional
    public void changePassword(MemberDto.PasswordUpdateReq passwordUpdateReq){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        if (passwordEncoder.matches(passwordUpdateReq.getExistPassword(), member.getPassword())) {
            updatePassword(member, passwordUpdateReq.getNewPassword());
        } else {
            throw new PojeException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }


    /**
     * 입력 정보 일치 여부 확인
     * @param passwordFindReq
     * @return member
     */
    @Transactional(readOnly = true)
    public Member checkPassword(MemberDto.PasswordFindReq passwordFindReq){
        Member member = memberRepository.findByEmail(passwordFindReq.getEmail()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        // 회원 정보가 일치하지 않으면 예외
        if (member.getNickName().equals(passwordFindReq.getNickName())) {
            return member;
        } else {
            throw new PojeException(ErrorCode.MEMBER_INFO_NOT_MATCH);
        }
    }


    /**
     * 비밀번호 업데이트
     * @param member
     * @param password
     */
    @Transactional
    public void updatePassword(Member member, String password){
        member.updatePassword(passwordEncoder.encode(password));
        memberRepository.save(member);
    }


    /**
     * 로그아웃 시 유효한 access token redis 블랙리스트로 추가
     * @param accessToken
     */
    @Transactional
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
     * @return : TokenDto
     */
    @Transactional
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