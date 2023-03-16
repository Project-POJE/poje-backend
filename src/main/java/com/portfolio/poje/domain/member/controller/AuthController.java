package com.portfolio.poje.domain.member.controller;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.config.jwt.TokenDto;
import com.portfolio.poje.domain.member.dto.MemberDto;
import com.portfolio.poje.domain.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;


    /**
     * 사용자 회원가입
     * @param memberJoinReq
     * @return
     */
    @PostMapping("/join")
    public ResponseEntity<BasicResponse> join(@RequestBody @Valid MemberDto.MemberJoinReq memberJoinReq){
        authService.join(memberJoinReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "회원가입 성공"));
    }


    /**
     * 사용자 로그인
     * @param memberLoginReq
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<BasicResponse> login(@RequestBody @Valid MemberDto.MemberLoginReq memberLoginReq, HttpServletResponse response){
        TokenDto tokenDto = authService.login(memberLoginReq);

        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.setHeader("RefreshToken", tokenDto.getRefreshToken());

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "로그인 성공"));
    }


    /**
     * 로그아웃
     * @return
     */
    @PostMapping("/member/logout")
    public ResponseEntity<BasicResponse> logout(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            // 로그아웃 시 Refresh Token 삭제
            authService.logout(bearerToken.substring(7));
        }

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "로그아웃 되었습니다."));
    }


    /**
     * access token 만료 시 재발행
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/reissue")
    public ResponseEntity<BasicResponse> reissue(HttpServletRequest request, HttpServletResponse response){
        String accessToken = request.getHeader("accessToken");
        String refreshToken = request.getHeader("refreshToken");

        TokenDto tokenDto = authService.reissue(accessToken, refreshToken);

        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.setHeader("RefreshToken", tokenDto.getRefreshToken());

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "재발급 되었습니다."));
    }



}
