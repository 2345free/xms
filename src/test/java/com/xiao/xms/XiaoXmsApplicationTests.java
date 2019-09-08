package com.xiao.xms;

import com.xiao.xms.util.ftp.jdk.FtpClientUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.net.ftp.FtpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XiaoXmsApplicationTests {

    @Autowired
    private FtpClientUtils ftpClientUtils;

    @Test
    public void testFtp() throws FileNotFoundException {
        FtpClient ftp = ftpClientUtils.getFtp();
        System.out.println(ftp.isConnected());
        System.out.println(ftp.isPassiveModeEnabled());
        File file = new File("C:\\Users\\mrluo\\Downloads\\FileZilla_3.44.2_win64-setup.exe");
        // 默认覆盖上传
        ftpClientUtils.upload("/", file.getName(), new FileInputStream(file));
        // 默认覆盖下载
        ftpClientUtils.download("/", file.getAbsolutePath(), file.getName());
    }

}
