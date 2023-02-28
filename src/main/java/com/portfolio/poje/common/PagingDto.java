package com.portfolio.poje.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PagingDto {

    private int page;         // 현재 페이지 번호
    private int recordSize;     // 한 화면에 출력할 데이터 수
    private int pageSize;    // 한 화면에 출력할 페이지 번호 수
    private int limit;    // 한 화면에 표시되는 오브젝트의 시작

    public PagingDto(int page) {
        this.page = page;
        this.recordSize = 12;
        this.pageSize = 5;
    }

    public void setPage(int page){
        this.page = page;
    }

    public int limitCalc(){
        // LIMIT 시작 위치 계산
        return limit = (page - 1) * recordSize;
    }

}
