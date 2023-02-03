package com.portfolio.poje.service.ability;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.controller.ability.licenseDto.LicenseCreateReq;
import com.portfolio.poje.controller.ability.licenseDto.LicenseInfoResp;
import com.portfolio.poje.controller.ability.licenseDto.LicenseUpdateReq;
import com.portfolio.poje.domain.ability.License;
import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.repository.ability.LicenseRepository;
import com.portfolio.poje.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
     * @return : List<LicenseInfoResp>
     */
    @Transactional
    public List<LicenseInfoResp> updateLicenseInfo(Long licenseId, LicenseUpdateReq licenseUpdateReq){
        License license = licenseRepository.findById(licenseId).orElseThrow(
                () -> new PojeException(ErrorCode.LICENSE_NOT_FOUND)
        );

        Member owner = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        license.updateInfo(licenseUpdateReq.getName());

        return owner.getLicenseList().stream()
                .map(l -> new LicenseInfoResp(l.getId(), l.getName()))
                .collect(Collectors.toList());
    }


    /**
     * 자격증 목록 반환
     * @return : List<LicenseInfoResp>
     */
    @Transactional(readOnly = true)
    public List<LicenseInfoResp> getLicenseList(){
        Member owner = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        return owner.getLicenseList().stream()
                .map(license -> new LicenseInfoResp(license.getId(), license.getName()))
                .collect(Collectors.toList());
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
