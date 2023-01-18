package com.portfolio.poje.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class BasicResponse {

    private int code;
    private String message;
    private List<Object> result = new ArrayList<>();


    // 데이터 o
    public BasicResponse(int code, String message, List<Object> result){
        this.code = code;
        this.message = message;
        this.result = result;
    }

    // 데이터 x
    public BasicResponse(int code, String message){
        this.code = code;
        this.message = message;
        this.result = Collections.emptyList();
    }

}
