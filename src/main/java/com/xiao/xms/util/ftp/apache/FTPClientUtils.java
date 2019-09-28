package com.xiao.xms.util.ftp.apache;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * FTP文件上传下载工具类
 *
 * @author luoxiaoxiao
 */
@Slf4j
@Data
@Component("ftpUtils")
@ConfigurationProperties(prefix = "ftp")
public class FTPClientUtils {

    private String host;
    private int port;
    private String username;
    private String password;

    private synchronized FTPClient makeConnection() throws IOException {
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            ftp.connect(host, port);
            ftp.setCharset(StandardCharsets.UTF_8);
            ftp.login(username, password);
            //开启被动模式
            ftp.enterLocalPassiveMode();
            ftp.getReplyCode();
            log.info("ftp connect success!");
        } finally {
            close(ftp);
        }
        return ftp;
    }

    @Retryable
    public String upload(String ftpPath, String filename, InputStream input) throws IOException {
        FTPClient ftp = null;
        try {
            ftp = makeConnection();
            if (!ftp.changeWorkingDirectory(ftpPath)) {
                ftp.makeDirectory(ftpPath);
                ftp.changeWorkingDirectory(ftpPath);
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.storeFile(filename, input);
            if (!ftpPath.endsWith("/")) {
                ftpPath += "/";
            }
        } finally {
            close(ftp);
        }
        return ftpPath + filename;
    }

    private void close(FTPClient ftp) {
        if (ftp == null) {
            return;
        }
        try {
            ftp.logout();
            if (ftp.isConnected()) {
                ftp.disconnect();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
