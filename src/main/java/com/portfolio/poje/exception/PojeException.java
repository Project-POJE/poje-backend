package com.portfolio.poje.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PojeException extends RuntimeException{

    private final ErrorCode errorCode;

}
