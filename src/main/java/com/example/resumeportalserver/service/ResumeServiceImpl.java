package com.example.resumeportalserver.service;

import com.example.resumeportalserver.model.UserFile;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Slf4j
@Service
public class ResumeServiceImpl implements ResumeService {
    @Value("${google.firebase.url}")
    private String DOWNLOAD_URL;

    @Value("${google.firebase.secret}")
    private String serviceKey;

    @Value("${google.firebase.name}")
    private String bucketName;

    @Override
    public String uploadUserFile(UserFile userFile, String fileName) throws IOException {
        BlobId blobId = BlobId.of("resume-portal-client.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        //figure out how to add on email
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(serviceKey));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(userFile.getResume().toPath()));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    @Override
    public UserFile convertToUserFile(MultipartFile multipartFile, String filename) throws FileNotFoundException {
        File tempFile = new File(filename);
        try (FileOutputStream fos = new FileOutputStream(tempFile)){
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserFile userFile = new UserFile(UUID.randomUUID(),"temp email",tempFile);
        return userFile;
    }
    @Override
    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    @Override
    public Object upload(MultipartFile multipartFile) {
        try{
            log.info(String.valueOf(multipartFile));
            String fileName = multipartFile.getOriginalFilename();
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

            UserFile userFile = this.convertToUserFile(multipartFile,fileName);
            String TEMP_URL = this.uploadUserFile(userFile, fileName);
            return TEMP_URL;
        }catch (Exception e){
            e.printStackTrace();
            return "nope";
        }
    }
}
