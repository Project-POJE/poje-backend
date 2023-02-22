package com.portfolio.poje.domain.ability.controller;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.domain.ability.dto.LicenseDto;
import com.portfolio.poje.domain.ability.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class LicenseController {

    private final LicenseService licenseService;

    /**
     * 자격증 등록
     * @param licenseCreateReq
     * @return
     */
    @PostMapping("/license")
    ResponseEntity<BasicResponse> createLicense(@RequestBody @Valid LicenseDto.LicenseCreateReq licenseCreateReq){
        licenseService.enroll(licenseCreateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "등록되었습니다."));
    }


    /**
     * 자격증 수정 (추가 or 삭제)
     * @param licenseUpdateReq
     * @return : List<LicenseInfoResp>
     */
    @PutMapping("/license")
    ResponseEntity<BasicResponse> updateLicense(@RequestBody LicenseDto.LicenseUpdateReq licenseUpdateReq){
        List<LicenseDto.LicenseInfoResp> licenseInfoRespList = licenseService.updateLicense(licenseUpdateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "수정되었습니다.", licenseInfoRespList));
    }


    /**
     * 자격증 목록 반환
     * @return : List<LicenseInfoResp>
     */
    @GetMapping("/license")
    ResponseEntity<BasicResponse> licenseInfo(){
        List<LicenseDto.LicenseInfoResp> licenseInfoRespList = licenseService.getLicenseList();

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "자격증 목록 조회", licenseInfoRespList));
    }


}
