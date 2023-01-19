package com.portfolio.poje.controller.ability;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.ability.licenseDto.LicenseListResponseDto;
import com.portfolio.poje.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class LicenseController {

    private final LicenseService licenseService;

    /**
     * 자격증 등록
     * @param licenseMap
     * @return
     */
    @PostMapping("/member/license")
    ResponseEntity<BasicResponse> createLicense(@RequestBody Map<String, String> licenseMap){
        licenseService.enroll(licenseMap.get("name"));

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "등록되었습니다."));
    }


    /**
     * 자격증 수정
     * @param licenseMap
     * @param licenseId
     * @return
     */
    @PutMapping("/member/license/{license_id}")
    ResponseEntity<BasicResponse> updateLicenseInfo(@RequestBody Map<String, String> licenseMap,
                                                @PathVariable(name = "license_id") Long licenseId){
        LicenseListResponseDto licenseListResponseDto = licenseService.updateLicenseInfo(licenseMap.get("name"), licenseId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.ACCEPTED.value(), "수정되었습니다.", licenseListResponseDto));
    }


    /**
     * 자격증 목록 반환
     * @return
     */
    @GetMapping("/member/license")
    ResponseEntity<BasicResponse> licenseInfo(){
        LicenseListResponseDto licenseListResponseDto = licenseService.getLicenseList();

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "자격증 목록 조회", licenseListResponseDto));
    }

}
