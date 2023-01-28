package com.portfolio.poje.controller.member;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.config.jwt.TokenDto;
import com.portfolio.poje.controller.member.memberDto.*;
import com.portfolio.poje.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    /**
     * 사용자 회원가입
     * @param memberJoinReq
     * @return
     */
    @Tag(name = "Members")
    @Operation(summary = "회원가입", description = "memberJoinRequest 필드들로 회원가입한다.", responses = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
    })
    @PostMapping("/join")
    public ResponseEntity<BasicResponse> join(@RequestBody @Validated MemberJoinReq memberJoinReq){
        memberService.join(memberJoinReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "회원가입 성공"));
    }


    /**
     * 로그인 아이디 중복 확인
     * @param loginId
     * @return
     */
    @GetMapping("/loginId/{loginId}")
    public ResponseEntity<BasicResponse> loginIdDuplicate(@PathVariable(value = "loginId") String loginId){
        BasicResponse basicResponse;
        if (memberService.loginIdCheck(loginId)){
            basicResponse = new BasicResponse(ErrorCode.BAD_REQUEST.getStatus().value(), "이미 존재하는 아이디입니다.");
        } else{
            basicResponse = new BasicResponse(HttpStatus.OK.value(), "사용할 수 있는 아이디입니다.");
        }

        return ResponseEntity.ok(basicResponse);
    }


    /**
     * 사용자 로그인
     * @param memberLoginReq
     * @param response
     * @return
     */
    @Tag(name = "Members")
    @Operation(summary = "로그인", description = "memberLoginRequest 필드들로 로그인한다.")
    @PostMapping("/login")
    public ResponseEntity<BasicResponse> login(@RequestBody @Validated MemberLoginReq memberLoginReq, HttpServletResponse response){
        TokenDto tokenDto = memberService.login(memberLoginReq);

        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.setHeader("Set-Cookie", setRefreshToken(tokenDto.getRefreshToken()).toString());

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "로그인 성공"));
    }


    /**
     * 사용자 정보 반환
     * @return : MemberInfoResp
     */
    @GetMapping("/member")
    public ResponseEntity<BasicResponse> getMemberInfo(){
        MemberInfoResp memberInfoResp = memberService.getMemberInfo(SecurityUtil.getCurrentMemberId());

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "회원 정보 조회", memberInfoResp));
    }


    /**
     * 사용자 정보 수정
     * @param memberUpdateReq
     * @return : MemberInfoResp
     */
    @PutMapping("/member")
    public ResponseEntity<BasicResponse> updateMemberInfo(@RequestBody MemberUpdateReq memberUpdateReq){
        MemberInfoResp memberInfoResp = memberService.updateMember(memberUpdateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.ACCEPTED.value(), "회원 정보가 수정되었습니다.", memberInfoResp));
    }


    /**
     * 로그아웃
     * @return
     */
    @PostMapping("/member/logout")
    public ResponseEntity<BasicResponse> logout(){
        // 로그아웃 시 Refresh Token 삭제
        memberService.deleteRefreshToken(SecurityUtil.getCurrentMemberId());
        
        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "로그아웃 되었습니다."));
    }


    /**
     * access token 만료 시 재발행
     * @param tokenReq
     * @param response
     * @return
     */
    @PostMapping("/reissue")
    public ResponseEntity<BasicResponse> reissue(@RequestBody TokenReq tokenReq, HttpServletResponse response){
        TokenDto tokenDto = memberService.reissue(tokenReq);

        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.setHeader("Set-Cookie", setRefreshToken(tokenDto.getRefreshToken()).toString());

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "재발급 되었습니다."));
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
