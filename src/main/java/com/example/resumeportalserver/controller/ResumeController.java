package com.example.resumeportalserver.controller;

import com.example.resumeportalserver.service.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/api/v1/resume-portal")
@CrossOrigin("http://localhost:3000/")
public class ResumeController {
    private ResumeService resumeService;

    @Autowired
    public  ResumeController(ResumeService resumeService){
        this.resumeService = resumeService;
    }
    @PostMapping("/upload")
    public Object object(@RequestParam("file")MultipartFile multipartFile){
        return resumeService.upload(multipartFile);
    }
}
