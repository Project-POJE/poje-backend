package com.portfolio.poje.service.ability;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.controller.ability.licenseDto.LicenseCreateReq;
import com.portfolio.poje.controller.ability.licenseDto.LicenseInfoReq;
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

import java.util.ArrayList;
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
     * 자격증 수정(추가) or 삭제
     * @param licenseUpdateReq
     */
    @Transactional
    public void updateLicense(LicenseUpdateReq licenseUpdateReq){
        Member owner = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        // 등록된 자격증 이름 List 생성
        List<String> licenseNameList = owner.getLicenseList().stream()
                        .map(l -> l.getName())
                        .collect(Collectors.toList());

        if (licenseNameList.isEmpty() && !licenseUpdateReq.getLicenseList().isEmpty()){ // 등록된 자격증이 없고, 전달받은 자격증이 있으면
            for (LicenseInfoReq licenseInfoReq : licenseUpdateReq.getLicenseList()){    // 전달받은 자격증 모두 저장
                License license = License.enrollLicense()
                        .owner(owner)
                        .name(licenseInfoReq.getName())
                        .build();

                licenseRepository.save(license);
            }
        } else if (!licenseNameList.isEmpty() && licenseUpdateReq.getLicenseList().isEmpty()){  // 등록된 자격증이 있고, 전달받은 자격증이 없으면
            for (License license : owner.getLicenseList()){ // 등록된 자격증 모두 삭제
                licenseRepository.delete(license);
            }
        } else if (!licenseNameList.isEmpty() && !licenseUpdateReq.getLicenseList().isEmpty()){ // 등록된 자격증이 있고, 전달받은 자격증도 있으면
            // 등록된 자격증 이름 추출
            for (String name: licenseNameList){
                // 전달받은 자격증 이름 List
                List<String> receiveNameList = new ArrayList<>();
                for (LicenseInfoReq licenseInfoReq: licenseUpdateReq.getLicenseList()){
                    receiveNameList.add(licenseInfoReq.getName());
                }

                // 전달받은 자격증 목록에 등록된 자격증이 없으면 삭제
                if (!receiveNameList.contains(name)){
                    licenseRepository.deleteByName(name);
                }
            }

            // 전달받은 자격증 이름 추출
            for (LicenseInfoReq licenseInfoReq: licenseUpdateReq.getLicenseList()){
                // 등록된 자격증 목록에 전달받은 자격증이 없으면 새로 추가
                if (!licenseNameList.contains(licenseInfoReq.getName())){
                    License license = License.enrollLicense()
                            .owner(owner)
                            .name(licenseInfoReq.getName())
                            .build();

                    licenseRepository.save(license);
                }
            }
        }

        // (영속 컨텍스트)
        // 반환해줄거면 licenseRepository.findByOwner(owner); 로 List<License> 데이터 불러온 후 반환
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


}
