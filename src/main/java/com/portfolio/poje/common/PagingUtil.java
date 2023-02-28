package com.portfolio.poje.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PagingUtil {

    private int page;
    private int totalRecordCnt;       // 전체 데이터 수
    private int totalPageCnt;      // 전체 페이지 수
    private int startPage;           // 첫 페이지 번호
    private int endPage;             // 끝 페이지 번호
    private boolean isPrev;                 // 이전 페이지 표시 여부
    private boolean isNext;                 // 다음 페이지 표시여부


    public PagingUtil(int totalRecordCnt, PagingDto pagingDto) {
        if (totalRecordCnt > 0){
            this.totalRecordCnt = totalRecordCnt;
            this.calculation(pagingDto);
        }
    }


    private void calculation(PagingDto pagingDto) {

        // 전체 페이지 수 계산
        totalPageCnt = ((totalRecordCnt - 1) / pagingDto.getRecordSize()) + 1;

        // 현재 페이지 번호가 전체 페이지 수보다 큰 경우, 현재 페이지 번호에 전체 페이지 수 저장
        if (pagingDto.getPage() > totalPageCnt) {
            pagingDto.setPage(totalPageCnt);
        }

        // 하단에 보여줄 첫 페이지 번호 계산
        startPage = ((pagingDto.getPage() - 1) / pagingDto.getPageSize()) * pagingDto.getPageSize() + 1;

        // 하단에 보여줄 끝 페이지 번호 계산
        endPage = startPage + pagingDto.getPageSize() - 1;

        // 끝 페이지가 전체 페이지 수보다 큰 경우, 끝 페이지 전체 페이지 수 저장
        if (endPage > totalPageCnt) {
            endPage = totalPageCnt;
        }

        // 이전 페이지 존재 여부 확인
        isPrev = startPage != 1;

        // 다음 페이지 존재 여부 확인
        isNext = (endPage * pagingDto.getRecordSize()) < totalRecordCnt;

        this.page = pagingDto.getPage();
    }


}
