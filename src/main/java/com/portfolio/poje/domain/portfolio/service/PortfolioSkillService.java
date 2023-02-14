package com.portfolio.poje.domain.portfolio.service;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.domain.portfolio.dto.PfSkillDto;
import com.portfolio.poje.domain.portfolio.repository.PortfolioSkillRepository;
import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import com.portfolio.poje.domain.portfolio.entity.PortfolioSkill;
import com.portfolio.poje.domain.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PortfolioSkillService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioSkillRepository portfolioSkillRepository;


    /**
     * 포트폴리오 사용 기술 추가
     * @param portfolioId
     * @param pfSkillCreateReq
     */
    @Transactional
    public void enroll(Long portfolioId, PfSkillDto.PfSkillCreateReq pfSkillCreateReq){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        for (PfSkillDto.PfSkillListReq skillSet : pfSkillCreateReq.getSkillSet()){
            for (PfSkillDto.PfSkillInfoReq skillInfo : skillSet.getSkills()){
                PortfolioSkill portfolioSkill = PortfolioSkill.builder()
                        .type(skillSet.getType())
                        .name(skillInfo.getName())
                        .path(skillInfo.getPath())
                        .portfolio(portfolio)
                        .build();

                portfolioSkillRepository.save(portfolioSkill);
            }
        }

    }


    /**
     * 포트폴리오 사용 기술 수정 (추가 or 삭제)
     * @param portfolioId
     * @param pfSkillUpdateReq
     */
    @Transactional
    public void updatePortfolioSkill(Long portfolioId, PfSkillDto.PfSkillUpdateReq pfSkillUpdateReq){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        List<PortfolioSkill> uploadSkills = portfolio.getPortfolioSkills();
        if (uploadSkills.isEmpty() && !pfSkillUpdateReq.getSkillSet().isEmpty()){   // 등록된 기술이 없고, 전달받은 목록이 있으면
            for (PfSkillDto.PfSkillListReq skillSet : pfSkillUpdateReq.getSkillSet()){     // 전달받은 목록 모두 저장
                for (PfSkillDto.PfSkillInfoReq skillInfo : skillSet.getSkills()){
                    PortfolioSkill portfolioSkill = PortfolioSkill.builder()
                            .type(skillSet.getType())
                            .name(skillInfo.getName())
                            .path(skillInfo.getPath())
                            .portfolio(portfolio)
                            .build();

                    portfolioSkillRepository.save(portfolioSkill);
                }
            }
        } else if (!uploadSkills.isEmpty() && pfSkillUpdateReq.getSkillSet().isEmpty()){    // 등록된 기술이 있고, 전달받은 목록이 없으면
            for (PortfolioSkill skill : uploadSkills){      // 등록된 기술 모두 삭제
                portfolioSkillRepository.delete(skill);
            }
            portfolio.getPortfolioSkills().clear();

        } else if (!uploadSkills.isEmpty() && !pfSkillUpdateReq.getSkillSet().isEmpty()){   // 등록된 기술이 있고, 전달받은 목록이 있으면
            // 등록된 기술 명 목록
            List<String> enrolledName = new ArrayList<>();
            List<String> receivedName = new ArrayList<>();
            List<String> deleteName = new ArrayList<>();

            // 등록된 기술 명 추출
            for (PortfolioSkill enrolledSkill : uploadSkills){
                enrolledName.add(enrolledSkill.getName());

                // 전달받은 목록에서 기술 명 추출
                for (PfSkillDto.PfSkillListReq pfSkillListReq : pfSkillUpdateReq.getSkillSet()){
                    for (PfSkillDto.PfSkillInfoReq pfSkillInfo : pfSkillListReq.getSkills()){
                        receivedName.add(pfSkillInfo.getName());
                    }
                }

                // 전달받은 목록에 등록된 기술이 존재하지 않으면 삭제
                if (!receivedName.contains(enrolledSkill.getName())){
                    deleteName.add(enrolledSkill.getName());
                    portfolioSkillRepository.delete(enrolledSkill);
                }
            }

            // 포트폴리오 사용 기술에서 삭제할 때 ConcurrentModificationException이 발생하므로 따로 삭제
            portfolio.getPortfolioSkills().removeAll(deleteName);

            // 전달받은 목록에서 기술 명 추출
            for (PfSkillDto.PfSkillListReq pfSkillListReq : pfSkillUpdateReq.getSkillSet()){
                for (PfSkillDto.PfSkillInfoReq pfSkillInfo : pfSkillListReq.getSkills()){
                    // 등록된 기술 목록에 전달받은 기술이 없으면 새로 추가
                    if (!enrolledName.contains(pfSkillInfo.getName())){
                        PortfolioSkill portfolioSkill = PortfolioSkill.builder()
                                .type(pfSkillListReq.getType())
                                .name(pfSkillInfo.getName())
                                .path(pfSkillInfo.getPath())
                                .portfolio(portfolio)
                                .build();

                        portfolioSkillRepository.save(portfolioSkill);
                    }
                }
            }
        }

    }


    /**
     * 포트폴리오 사용 기술 목록 반환
     * @param portfolioId
     * @return : List<PfSkillListResp>
     */
    @Transactional(readOnly = true)
    public List<PfSkillDto.PfSkillListResp> getPortfolioSkills(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        List<PfSkillDto.PfSkillListResp> pfSkillList = new ArrayList<>();

        // 포트폴리오에서 사용하는 기술들의 type 목록을 가져옴
        List<String> skillTypeList = portfolioSkillRepository.findDistinctTypeByPortfolio(portfolio);
        for (String type : skillTypeList){
            List<PfSkillDto.PfSkillInfoResp> pfSkillInfoList = new ArrayList<>();

            List<PortfolioSkill> skills = portfolioSkillRepository.findByPortfolioAndType(portfolio, type);
            for (PortfolioSkill skill : skills){
                pfSkillInfoList.add(new PfSkillDto.PfSkillInfoResp(skill.getId(), skill.getName(), skill.getPath()));
            }
            pfSkillList.add(new PfSkillDto.PfSkillListResp(type, pfSkillInfoList));
        }

        return pfSkillList;
    }



}
