package com.portfolio.poje.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class BasicResponse {

    private Integer code;
    private String message;
    private List<Object> result = new ArrayList<>();


    // 데이터 o, HttpStatus.OK
    public BasicResponse(String message, List<Object> result){
        this.code = HttpStatus.OK.value();
        this.message = message;
        this.result = result;
    }

    // 데이터 x, HttpStatus.OK
    public BasicResponse(String message){
        this.code = HttpStatus.OK.value();
        this.message = message;
        this.result = Collections.emptyList();
    }

    // etc
    public BasicResponse(Integer code, String message){
        this.code = code;
        this.message = message;
        this.result = Collections.emptyList();
    }

}
