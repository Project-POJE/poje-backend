package com.portfolio.poje.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Poje custom Exception
     * @param e
     * @return
     */
    @ExceptionHandler(PojeException.class)
    protected ResponseEntity<ErrorResponse> handlePojeException(final PojeException e){
        log.error("handlePojeException: {}", e.getErrorCode());

        return ResponseEntity
                .status(e.getErrorCode().getStatus().value())
                .body(new ErrorResponse(e.getErrorCode()));
    }

    /**
     * HTTP 405 Exception
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e){
        log.error("handleHttpRequestMethodNotSupportedException: {}", e.getMessage());

        return ResponseEntity
                .status(ErrorCode.METHOD_NOT_ALLOWED.getStatus().value())
                .body(new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED));
    }


    /**
     * 유효성 검사 실패 시
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e){
        log.error("handleMethodArgumentNotValidException: {}", e.getMessage());

        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getStatus().value())
                .body(new ErrorResponse(ErrorCode.BAD_REQUEST));
    }


    /**
     * 로그인 잘못된 정보 입력 시
     * @return
     */
    @ExceptionHandler({InternalAuthenticationServiceException.class, BadCredentialsException.class})
    protected ResponseEntity<ErrorResponse> handleInternalAuthenticationServiceException(){

        return ResponseEntity
                .status(ErrorCode.MEMBER_NOT_MATCH.getStatus().value())
                .body(new ErrorResponse(ErrorCode.MEMBER_NOT_MATCH));
    }


    /**
     * HTTP 500 Exception
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(final Exception e){
        log.error("handleException: {}", e.getMessage());

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value())
                .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }

}
