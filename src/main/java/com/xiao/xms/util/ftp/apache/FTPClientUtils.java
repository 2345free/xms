package com.xiao.xms.util.ftp.apache;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * FTP文件上传下载工具类
 *
 * @author luoxiaoxiao
 */

@Component("ftpUtils")
@ConfigurationProperties(prefix = "ftp")
public class FTPClientUtils {

    private static final Logger log = LoggerFactory.getLogger(FTPClientUtils.class);

    private String host;
    private int port;
    private String username;
    private String password;

    private FTPClient ftp;

    private synchronized FTPClient makeConnection() {
        if (ftp == null || !ftp.isConnected()) {
            try {
                ftp = new FTPClient();
                ftp.connect(host, port);
                ftp.setCharset(Charset.forName("utf-8"));
                ftp.login(username, password);
                //开启被动模式
                ftp.enterLocalPassiveMode();
                int reply = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.logout();
                }
            } catch (Exception e) {
                log.error("ftp登录失败，检测登录ip，端口号，用户名密码是否正确，错误信息为" + e.getMessage());
            }
            log.info("ftp服务器连接成功");
        }
        return ftp;
    }

    @Retryable
    public String upload(String ftpPath, String filename, InputStream input) throws IOException {
        FTPClient ftp = makeConnection();
        if (!ftp.changeWorkingDirectory(ftpPath)) {
            ftp.makeDirectory(ftpPath);
            ftp.changeWorkingDirectory(ftpPath);
        }
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.storeFile(filename, input);
        if (!ftpPath.endsWith("/")) {
            ftpPath += "/";
        }
        return ftpPath + filename;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
