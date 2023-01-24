package com.portfolio.poje.service.ability;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.controller.ability.licenseDto.LicenseListResponseDto;
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
     * @param name
     */
    @Transactional
    public void enroll(String name){
        Member owner = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        if (licenseRepository.existsByName(name)){
            throw new PojeException(ErrorCode.LICENSE_ALREADY_ENROLL);
        }

        License license = License.enrollLicense()
                .name(name)
                .owner(owner)
                .build();

        licenseRepository.save(license);
    }


    /**
     * 자격증 수정 후 목록 반환
     * @param name
     * @param licenseId
     * @return : LicenseListResponseDto
     */
    @Transactional
    public LicenseListResponseDto updateLicenseInfo(String name, Long licenseId){
        License license = licenseRepository.findById(licenseId).orElseThrow(
                () -> new PojeException(ErrorCode.LICENSE_NOT_FOUND)
        );

        Member owner = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        license.updateInfo(name);

        return new LicenseListResponseDto(owner);
    }


    /**
     * 자격증 목록 반환
     * @return : LicenseListResponseDto
     */
    @Transactional(readOnly = true)
    public LicenseListResponseDto getLicenseList(){
        Member owner = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        return new LicenseListResponseDto(owner);
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
