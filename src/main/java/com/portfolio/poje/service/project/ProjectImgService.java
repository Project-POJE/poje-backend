package com.portfolio.poje.service.project;

import com.portfolio.poje.common.FileHandler;
import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.domain.project.ProjectImg;
import com.portfolio.poje.repository.project.ProjectImgRepository;
import com.portfolio.poje.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectImgService {

    private final ProjectRepository projectRepository;
    private final ProjectImgRepository projectImgRepository;
    private final FileHandler fileHandler;


    @Transactional
    public void enrollImages(Long projectId, List<MultipartFile> files) throws Exception{
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new PojeException(ErrorCode.PROJECT_NOT_FOUND)
        );

        List<ProjectImg> projectImgList = fileHandler.parseFileInfo(files);

        // 파일이 존재할 때만 처리
        if (!projectImgList.isEmpty()){
            for (ProjectImg img: projectImgList){
                img.addProject(project);
                projectImgRepository.save(img);
            }
        }

    }


}
