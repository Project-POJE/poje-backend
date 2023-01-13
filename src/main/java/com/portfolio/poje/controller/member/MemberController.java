package com.portfolio.poje.controller.member;

import com.portfolio.poje.config.jwt.TokenDto;
import com.portfolio.poje.controller.member.memberDto.MemberJoinRequestDto;
import com.portfolio.poje.controller.member.memberDto.MemberLoginRequestDto;
import com.portfolio.poje.controller.member.memberDto.TokenRequestDto;
import com.portfolio.poje.repository.MemberRepository;
import com.portfolio.poje.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    /**
     * 사용자 회원가입
     * @param memberJoinRequestDto
     * @return
     */
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody MemberJoinRequestDto memberJoinRequestDto){
        memberService.join(memberJoinRequestDto);

        return ResponseEntity.ok(true);
    }


    /**
     * 사용자 로그인
     * @param memberLoginRequestDto
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response){
        memberRepository.findByLoginId(memberLoginRequestDto.getLoginId()).orElseThrow(
                () -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다.")
        );

        TokenDto tokenDto = memberService.login(memberLoginRequestDto);

        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.setHeader("Set-Cookie", setRefreshToken(tokenDto.getRefreshToken()).toString());

        return ResponseEntity.ok(true);
    }


    @PostMapping("/reissue")
    public ResponseEntity reissue(@RequestBody TokenRequestDto tokenRequestDto, HttpServletResponse response){
        TokenDto tokenDto = memberService.reissue(tokenRequestDto);

        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.setHeader("Set-Cookie", setRefreshToken(tokenDto.getRefreshToken()).toString());

        return ResponseEntity.ok(true);
    }


    public ResponseCookie setRefreshToken(String refreshToken){
        ResponseCookie cookie = ResponseCookie.from("RefreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .maxAge(60 * 60 * 24)
                .sameSite("None")
                .path("/")
                .build();

        return cookie;
    }

}
