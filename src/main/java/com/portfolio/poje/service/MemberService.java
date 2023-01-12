package com.portfolio.poje.service;

import com.portfolio.poje.config.jwt.JwtTokenProvider;
import com.portfolio.poje.config.jwt.TokenDto;
import com.portfolio.poje.controller.member.memberDto.MemberJoinRequestDto;
import com.portfolio.poje.controller.member.memberDto.MemberLoginRequestDto;
import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.domain.member.RoleType;
import com.portfolio.poje.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
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
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        return tokenDto;
    }


}
