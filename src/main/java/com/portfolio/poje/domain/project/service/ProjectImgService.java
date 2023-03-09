package com.portfolio.poje.domain.project.service;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.aws.S3FileUploader;
import com.portfolio.poje.domain.project.dto.PrImgDto;
import com.portfolio.poje.domain.project.entity.Project;
import com.portfolio.poje.domain.project.repository.ProjectImgRepository;
import com.portfolio.poje.domain.project.entity.ProjectImg;
import com.portfolio.poje.domain.project.repository.ProjectRepository;
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
    private final S3FileUploader fileUploader;


    /**
     * 프로젝트 이미지 추가 및 삭제
     * @param projectId
     * @param prImgDelListReq
     * @param files
     * @throws Exception
     */
    @Transactional
    public void updateImages(Long projectId, PrImgDto.PrImgDelListReq prImgDelListReq, List<MultipartFile> files) throws Exception{
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        // 삭제한 경로들을 받아서 지워줌
        if (prImgDelListReq != null){
            for (String delUrl : prImgDelListReq.getPrImgDelList()){
                // 삭제할 경로로 ProjectImg 조회
                ProjectImg projectImg = projectImgRepository.findByUrl(delUrl).orElseThrow(
                        () -> new PojeException(ErrorCode.IMG_NOT_FOUND)
                );

                // S3에서 파일 삭제
                fileUploader.deleteFile(delUrl, "project");
                // DB에서 파일 삭제
                project.getProjectImgs().remove(projectImg);
                projectImgRepository.delete(projectImg);
            }
        }

        List<ProjectImg> projectImgList = new ArrayList<>();
        if (files != null){
            List<String> urls = fileUploader.uploadFiles(files, "project");
            for (String url: urls){
                projectImgList.add(ProjectImg.enrollProjectImg()
                        .url(url)
                        .build());
            }
        }

        // 파일이 존재할 때만 처리
        if (!projectImgList.isEmpty()){
            for (ProjectImg img: projectImgList){
                img.addProject(project);
                projectImgRepository.save(img);
            }
        }
    }

    
}
