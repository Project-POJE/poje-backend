package com.portfolio.poje.service.project;

import com.portfolio.poje.common.FileHandler;
import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.domain.project.ProjectImg;
import com.portfolio.poje.repository.project.ProjectImgRepository;
import com.portfolio.poje.repository.project.ProjectRepository;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectImgService {

    private final ProjectRepository projectRepository;
    private final ProjectImgRepository projectImgRepository;
    private final FileHandler fileHandler;


    /**
     * 프로젝트 이미지 업로드
     * @param projectId
     * @param files
     * @throws Exception
     */
    @Transactional
    public void enrollImages(Long projectId, List<MultipartFile> files) throws Exception{
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        List<ProjectImg> projectImgList = fileHandler.parseFileInfo(project, files);

        // 파일이 존재할 때만 처리
        if (!projectImgList.isEmpty()){
            for (ProjectImg img: projectImgList){
                img.addProject(project);
                projectImgRepository.save(img);
            }
        }

    }


    /**
     * 프로젝트 이미지 추가 및 삭제
     * @param projectId
     * @param files
     * @throws Exception
     */
    @Transactional
    public void updateImages(Long projectId, List<MultipartFile> files) throws Exception{
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        // 업로드된 프로젝트 이미지 목록 불러오기
        List<ProjectImg> savedProjectImgList = projectImgRepository.findByProject(project);
        // 새롭게 전달된 파일들의 목록을 저장할 List
        List<MultipartFile> addFileList = new ArrayList<>();

        if (Collections.isEmpty(savedProjectImgList)){  // 업로드된 이미지가 존재하지 않고
            if (!Collections.isEmpty(files)) {    // 전달받은 파일이 존재하면
                for (MultipartFile file : files)
                    addFileList.add(file);  // 파일 목록에 추가
            }
        } else {    // 업로드된 이미지가 존재하고
            if (Collections.isEmpty(files)){    // 전달받은 파일이 존재하지 않으면
                for (ProjectImg projectImg : savedProjectImgList) {
                    fileHandler.deleteProjectImg(projectId, projectImg.getFilePath());
                    projectImgRepository.delete(projectImg);    // 업로드된 이미지 삭제
                }
            } else {    // 전달받은 파일이 존재
                // 업로드된 이미지 원본명 목록
                List<String> savedProjectImgOriginNameList = new ArrayList<>();

                // 업로드된 이미지 원본명 추출
                for (ProjectImg projectImg : savedProjectImgList){
                    String originalName = projectImg.getOriginalName();

                    // 전달받은 이미지 원본명 목록
                    List<String> filesOriginalNameList = new ArrayList<>();
                    for (MultipartFile file : files){
                        filesOriginalNameList.add(file.getOriginalFilename());
                    }

                    if (!filesOriginalNameList.contains(originalName)) {  // 전달받은 이미지 중 업로드된 파일이 존재하지 않으면
                        fileHandler.deleteProjectImg(projectId, projectImg.getFilePath());
                        projectImgRepository.delete(projectImg);    // 업로드된 이미지 삭제
                    }
                    else {
                        savedProjectImgOriginNameList.add(originalName);
                    }
                }

                for (MultipartFile file : files){
                    String fileOriginalName = file.getOriginalFilename();
                    if (!savedProjectImgOriginNameList.contains(fileOriginalName))  // 업로드된 파일이 아니면
                        addFileList.add(file);  // 저장할 이미지 목록에 추가
                }
            }
        }

        List<ProjectImg> projectImgList = fileHandler.parseFileInfo(project, addFileList);
        // 파일이 존재할 때만 처리
        if (!projectImgList.isEmpty()){
            for (ProjectImg img: projectImgList){
                img.addProject(project);
                projectImgRepository.save(img);
            }
        }
    }


}
