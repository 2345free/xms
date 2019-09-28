package com.xiao.xms.controller;

import com.xiao.xms.util.ftp.jdk.FtpClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.net.ftp.FtpProtocolException;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UploadController {

    private final FtpClientUtils ftpClientUtils;

    @PostMapping(value = "/upload", consumes = "multipart/*", headers = "Content-Type=multipart/form-data")
    public void upload(@RequestParam("files") MultipartFile[] files) throws IOException, FtpProtocolException {
        for (MultipartFile file : files) {
            String uploadPath = ftpClientUtils.upload("/", file.getOriginalFilename(), file.getInputStream());
            log.info(uploadPath);
        }
    }
}
