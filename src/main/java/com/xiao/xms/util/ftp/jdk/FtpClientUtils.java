package com.xiao.xms.util.ftp.jdk;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author luoxiaoxiao
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "ftp")
public class FtpClientUtils {

    private String host;
    private int port;
    private String username;
    private String password;

    /**
     * 连接ftp服务器 JDK 1.7
     */
    public FtpClient getFtp() throws IOException, FtpProtocolException {
        //创建地址
        SocketAddress addr = new InetSocketAddress(host, port);
        //连接
        FtpClient ftp = FtpClient.create();
        ftp.connect(addr);
        //登陆
        ftp.login(username, password.toCharArray());
        ftp.setBinaryType();
        log.info(ftp.getWelcomeMsg());
        return ftp;
    }

    /**
     * 上传文件
     */
    public String upload(String ftpPath, String fileName, InputStream in) throws IOException, FtpProtocolException {
        OutputStream os = null;
        try {
            FtpClient ftp = getFtp();
            ftp.changeDirectory(ftpPath);
            //将ftp文件加入输出流中。输出到ftp上
            os = ftp.putFileStream(fileName);
            //创建一个缓冲区
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in.read(bytes)) != -1) {
                os.write(bytes, 0, c);
            }
            log.info("upload success!");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        if (!ftpPath.endsWith("/")) {
            ftpPath += "/";
        }
        return ftpPath + fileName;
    }

    /**
     * 文件下载
     */
    public void download(String ftpPath, String localFile, String ftpFile) throws IOException, FtpProtocolException {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            FtpClient ftp = getFtp();
            ftp.changeDirectory(ftpPath);
            //获取ftp上的文件
            is = ftp.getFileStream(ftpFile);
            File file = new File(localFile);
            byte[] bytes = new byte[1024];
            int i;
            fos = new FileOutputStream(file);
            while ((i = is.read(bytes)) != -1) {
                fos.write(bytes, 0, i);
            }
            log.info("download success!");
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
