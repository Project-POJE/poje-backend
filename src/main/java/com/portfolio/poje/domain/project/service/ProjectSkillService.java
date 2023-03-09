package com.portfolio.poje.domain.project.service;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.domain.project.dto.PrSkillDto;
import com.portfolio.poje.domain.project.entity.Project;
import com.portfolio.poje.domain.project.repository.ProjectRepository;
import com.portfolio.poje.domain.project.repository.ProjectSkillRepository;
import com.portfolio.poje.domain.project.entity.ProjectSkill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectSkillService {

    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;


    /**
     * 프로젝트 사용 기술 수정 (추가 or 삭제)
     * @param projectId
     * @param skillList
     */
    @Transactional
    public void updateProjectSkill(Long projectId, List<PrSkillDto.PrSkillListReq> skillList){
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        List<ProjectSkill> uploadSkills = project.getProjectSkills();
        if (uploadSkills.isEmpty() && !skillList.isEmpty()){   // 등록된 기술이 없고, 전달받은 목록이 있으면
            for (PrSkillDto.PrSkillListReq skillSet : skillList){     // 전달받은 목록 모두 저장
                for (PrSkillDto.PrSkillInfoReq skillInfo : skillSet.getSkills()){
                    ProjectSkill projectSkill = ProjectSkill.builder()
                            .type(skillSet.getType())
                            .name(skillInfo.getName())
                            .project(project)
                            .build();

                    projectSkillRepository.save(projectSkill);
                }
            }

        } else if (!uploadSkills.isEmpty() && skillList.isEmpty()){    // 등록된 기술이 있고, 전달받은 목록이 없으면
            for (ProjectSkill skill : uploadSkills){      // 등록된 기술 모두 삭제
                projectSkillRepository.delete(skill);
            }
            project.getProjectSkills().clear();

        } else if (!uploadSkills.isEmpty() && !skillList.isEmpty()){   // 등록된 기술이 있고, 전달받은 목록이 있으면
            // 등록된 기술 명 목록
            List<String> enrolledName = new ArrayList<>();
            List<String> receivedName = new ArrayList<>();
            List<String> deleteName = new ArrayList<>();

            // 등록된 기술 명 추출
            for (ProjectSkill enrolledSkill : uploadSkills){
                enrolledName.add(enrolledSkill.getName());

                // 전달받은 목록에서 기술 명 추출
                for (PrSkillDto.PrSkillListReq prSkillListReq : skillList){
                    for (PrSkillDto.PrSkillInfoReq prSkillInfo : prSkillListReq.getSkills()){
                        receivedName.add(prSkillInfo.getName());
                    }
                }

                // 전달받은 목록에 등록된 기술이 존재하지 않으면 삭제
                if (!receivedName.contains(enrolledSkill.getName())){
                    deleteName.add(enrolledSkill.getName());
                    projectSkillRepository.delete(enrolledSkill);
                }
            }

            // 프로젝트 사용 기술에서 삭제할 때 ConcurrentModificationException이 발생하므로 따로 삭제
            project.getProjectSkills().removeAll(deleteName);

            // 전달받은 목록에서 기술 명 추출
            for (PrSkillDto.PrSkillListReq skillSet : skillList){
                for (PrSkillDto.PrSkillInfoReq skillInfo : skillSet.getSkills()){
                    // 등록된 기술 목록에 전달받은 기술이 없으면 새로 추가
                    if (!enrolledName.contains(skillInfo.getName())){
                        ProjectSkill projectSkill = ProjectSkill.builder()
                                .type(skillSet.getType())
                                .name(skillInfo.getName())
                                .project(project)
                                .build();

                        projectSkillRepository.save(projectSkill);
                    }
                }
            }
        }

    }



    /**
     * 프로젝트 사용 기술 타입별 정렬
     * @return : List<PrSkillListResp>
     */
    @Transactional(readOnly = true)
    public List<PrSkillDto.PrSkillListResp> toPrSkillListDto(Long projectId){

        List<PrSkillDto.PrSkillListResp> prSkillList = new ArrayList<>();

        List<String> skillTypeList = projectSkillRepository.findDistinctTypeById(projectId);
        for (String type : skillTypeList){
            List<PrSkillDto.PrSkillInfoResp> prSkillInfoList = new ArrayList<>();

            List<ProjectSkill> skills = projectSkillRepository.findByProjectIdAndType(projectId, type);
            for (ProjectSkill skill : skills){
                prSkillInfoList.add(new PrSkillDto.PrSkillInfoResp(skill.getId(), skill.getName()));
            }
            prSkillList.add(new PrSkillDto.PrSkillListResp(type, prSkillInfoList));
        }

        return prSkillList;
    }


}
