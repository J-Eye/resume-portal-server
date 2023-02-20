package com.example.resumeportalserver.service;

import com.example.resumeportalserver.model.UserFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ResumeService {
    String uploadUserFile(UserFile userFile, String fileName) throws IOException;
    UserFile convertToUserFile(MultipartFile multipartFile, String filename) throws FileNotFoundException;
    String getExtension(String fileName);
    Object upload(MultipartFile multipartFile);

}
