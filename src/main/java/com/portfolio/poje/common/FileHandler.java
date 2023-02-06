package com.portfolio.poje.common;

import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.project.Project;
import com.portfolio.poje.domain.project.ProjectImg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileHandler {

    private static final String separator = File.separator;

    // 사용자 프로필 사진 업로드
    public String uploadProfileImg(Member member, MultipartFile multipartFile) throws Exception{
        return uploadFile("profileImg", multipartFile, member.getId());
    }

    // 포트폴리오 배경 이미지 업로드
    public String uploadBackgroundImg(Portfolio portfolio, MultipartFile multipartFile) throws Exception{
        return uploadFile("backgroundImg", multipartFile, portfolio.getId());
    }


    // 단일 파일 업로드
    private String uploadFile(String name, MultipartFile multipartFile, Long id) throws Exception{
        File file = new File("");
        String rootPath = file.getAbsolutePath().split("poje")[0];
        // 파일을 저장할 세부 경로 지정 (프로젝트 id로 폴더 생성)
        String savePath = rootPath + name + separator + name + id;

        // 디렉터리가 존재하지 않은 경우
        if (!new File(savePath).exists()) {
            boolean wasSuccessful = new File(savePath).mkdirs();
            // 디렉터리 생성에 실패했을 경우
            if (!wasSuccessful)
                log.warn("업로드 폴더 생성에 실패했습니다.");
        }

        String originFileName = multipartFile.getOriginalFilename();
        // 파일명 중복을 피하고자 UUID 사용
        String saveFileName = UUID.randomUUID() + originFileName.substring(originFileName.lastIndexOf("."));
        String filePath = savePath + separator + saveFileName;

        // 업로드 한 파일 데이터를 지정한 경로에 저장
        multipartFile.transferTo(new File(filePath));

        // 파일 권한 설정 (쓰기, 읽기)
        file.setWritable(true);
        file.setReadable(true);

        return filePath;
    }


    // 프로젝트 이미지 업로드
    public List<ProjectImg> uploadProjectImg(Project project, List<MultipartFile> multipartFiles) throws Exception{
        // 반환할 파일 리스트
        List<ProjectImg> fileList = new ArrayList<>();

        File file = new File("");
        String rootPath = file.getAbsolutePath().split("poje")[0];
        // 파일을 저장할 세부 경로 지정 (프로젝트 id로 폴더 생성)
        String savePath = rootPath + "projectImg" + separator + "projectImg" + project.getId();

        // 디렉터리가 존재하지 않은 경우
        if (!new File(savePath).exists()) {
            boolean wasSuccessful = new File(savePath).mkdirs();
            // 디렉터리 생성에 실패했을 경우
            if (!wasSuccessful)
                log.warn("업로드 폴더 생성에 실패했습니다.");
        }

        // 다중 파일 처리
        for (MultipartFile multipartFile : multipartFiles){
            // 확장자명이 존재하지 않을 경우 처리 x
            if (ObjectUtils.isEmpty(multipartFile.getContentType())) break;

            String originFileName = multipartFile.getOriginalFilename();
            // 파일명 중복을 피하고자 UUID 사용
            String saveFileName = UUID.randomUUID() + originFileName.substring(originFileName.lastIndexOf("."));
            String filePath = savePath + separator + saveFileName;

            // ProjectImg 객체 생성
            ProjectImg projectImg = ProjectImg.enrollProjectImg()
                    .originalName(originFileName)
                    .filePath(filePath)
                    .build();

            fileList.add(projectImg);

            // 업로드 한 파일 데이터를 지정한 경로에 저장
            multipartFile.transferTo(new File(filePath));

            // 파일 권한 설정 (쓰기, 읽기)
            file.setWritable(true);
            file.setReadable(true);
        }

        return fileList;
    }


    // 업로드 폴더에서 이미지 제거
    public void deleteImg(String name, Long id, String filePath){
        //파일 경로 지정
        File file = new File("");
        String rootPath = file.getAbsolutePath().split("poje")[0];
        // 파일이 저장된 경로
        String savedPath = rootPath + separator + name + separator + name + id;

        // 업로드된 파일명(uuid)과 확장자만 추출
        String fileName = filePath.split(name + id)[1];
        log.info("fileName: {}", fileName);

        // 업로드 폴더에 존재하는 파일객체를 만듦
        File savedFile = new File(savedPath + "\\" + fileName);

        if(savedFile.exists()) { // 파일이 존재하면
            savedFile.delete(); // 파일 삭제
        }
    }

}
