package com.portfolio.poje.service.ability;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.controller.ability.licenseDto.LicenseCreateReq;
import com.portfolio.poje.controller.ability.licenseDto.LicenseListResp;
import com.portfolio.poje.controller.ability.licenseDto.LicenseUpdateReq;
import com.portfolio.poje.domain.ability.License;
import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.repository.ability.LicenseRepository;
import com.portfolio.poje.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final MemberRepository memberRepository;


    /**
     * 자격증 등록
     * @param licenseCreateReq
     */
    @Transactional
    public void enroll(LicenseCreateReq licenseCreateReq){
        Member owner = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        if (licenseRepository.existsByName(licenseCreateReq.getName())){
            throw new PojeException(ErrorCode.LICENSE_ALREADY_ENROLL);
        }

        License license = License.enrollLicense()
                .name(licenseCreateReq.getName())
                .owner(owner)
                .build();

        licenseRepository.save(license);
    }


    /**
     * 자격증 수정 후 목록 반환
     * @param licenseId
     * @param licenseUpdateReq
     * @return : LicenseListResp
     */
    @Transactional
    public LicenseListResp updateLicenseInfo(Long licenseId, LicenseUpdateReq licenseUpdateReq){
        License license = licenseRepository.findById(licenseId).orElseThrow(
                () -> new PojeException(ErrorCode.LICENSE_NOT_FOUND)
        );

        Member owner = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        license.updateInfo(licenseUpdateReq.getName());

        return new LicenseListResp(owner);
    }


    /**
     * 자격증 목록 반환
     * @return : LicenseListResp
     */
    @Transactional(readOnly = true)
    public LicenseListResp getLicenseList(){
        Member owner = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        return new LicenseListResp(owner);
    }


    /**
     * 자격증 삭제
     * @param licenseId
     */
    @Transactional
    public void deleteLicense(Long licenseId){
        License license = licenseRepository.findById(licenseId).orElseThrow(
                () -> new PojeException(ErrorCode.LICENSE_NOT_FOUND)
        );

        licenseRepository.delete(license);
    }

}
