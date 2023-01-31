package com.portfolio.poje.common;

import com.portfolio.poje.domain.project.ProjectImg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileHandler {

    public List<ProjectImg> parseFileInfo(List<MultipartFile> multipartFiles) throws Exception{
        // 반환할 파일 리스트
        List<ProjectImg> fileList = new ArrayList<>();

        // 전달된 파일이 존재하는 경우
        if (!CollectionUtils.isEmpty(multipartFiles)) {
            String separator = File.separator;
            // 파일을 업로드 한 날짜로 폴더 생성
            String today = new SimpleDateFormat("yyMMdd").format(new Date());

            File file = new File("");
            String rootPath = file.getAbsolutePath().split("poje")[0];
            // 파일을 저장할 세부 경로 지정
            String savePath = rootPath + separator + "projectImg" + separator + today;

            // 디렉터리가 존재하지 않은 경우
            if (!new File(savePath).exists()) {
                boolean wasSuccessful = new File(savePath).mkdirs();
                // 디렉터리 생성에 실패했을 경우
                if (!wasSuccessful)
                    log.warn("file: was not successful");
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
                        .fileSize(multipartFile.getSize())
                        .build();

                fileList.add(projectImg);

                // 업로드 한 파일 데이터를 지정한 경로에 저장
                multipartFile.transferTo(new File(filePath));

                // 파일 권한 설정 (쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
            }
        }

        return fileList;
    }

}
