package com.portfolio.poje.config.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3FileUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3Client;


    // S3에 파일 여러 장 업로드
    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String dirName) throws IOException {
        List<String> urls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles){
            urls.add(uploadFile(multipartFile, dirName));
        }

        return urls;
    }

    // S3에 파일 업로드
    public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException {
        // 파일 변환할 수 없으면 에러
        File uploadFile = convert(multipartFile).orElseThrow(
                () -> new IllegalArgumentException("Error: 파일 변환에 실패했습니다.")
        );

        String originalFileName = uploadFile.getName();

        // S3에 저장할 파일 명
        String fileName = dirName + "/" + UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));

        // s3로 업로드
        String uploadImageUrl = putS3(uploadFile, fileName);

        // 로컬에서 이미지 제거
        removeNewFile(uploadFile);

        return uploadImageUrl;
    }


    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 삭제
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 성공적으로 삭제되었습니다.");
            return;
        }
        log.info("파일 삭제에 실패하였습니다.");
    }

    // 로컬에 파일생성
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }


    // S3에서 파일 삭제
    public void deleteFile(String fileName, String dirName) {
        String uploadFileName = fileName.split(dirName + "/")[1];
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, dirName + "/" + uploadFileName));
    }



}
