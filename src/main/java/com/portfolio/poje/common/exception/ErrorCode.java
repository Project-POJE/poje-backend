package com.portfolio.poje.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 BAD_REQUEST: 잘못된 요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 요청입니다."),

    // 500 INTERNAL_SERVER_ERROR: 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),


    // Auth 관련
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "리프레시 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_VALIDATE(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "리프레시 토큰이 일치하지 않습니다."),
    ACCESS_TOKEN_NOT_VALIDATE(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다."),


    // Member 관련
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원이 존재하지 않습니다."),
    MEMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호를 잘못 입력했습니다. 입력하신 내용을 다시 확인해주세요."),

    // License 관련
    LICENSE_ALREADY_ENROLL(HttpStatus.BAD_REQUEST, "이미 등록된 자격증입니다."),
    LICENSE_NOT_FOUND(HttpStatus.BAD_REQUEST, "자격증이 존재하지 않습니다."),

    // Project 관련
    PROJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "프로젝트가 존재하지 않습니다."),
    AWARD_NOT_FOUND(HttpStatus.BAD_REQUEST, "수상 정보가 존재하지 않습니다."),
    SKILL_NOT_FOUND(HttpStatus.BAD_REQUEST, "기술 정보가 존재하지 않습니다."),


    // Portfolio 관련
    PORTFOLIO_NOT_FOUND(HttpStatus.BAD_REQUEST, "포트폴리오가 존재하지 않습니다"),


    ;

    private final HttpStatus status;
    private final String message;

}
