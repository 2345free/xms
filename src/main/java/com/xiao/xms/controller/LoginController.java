package com.xiao.xms.controller;

import com.xiao.xms.config.AppProperties;
import com.xiao.xms.config.CasProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: luoxiaoxiao
 */
@Slf4j
@Controller
public class LoginController {

    private static final String LOGOUTED_URL = "/logouted";

    @Autowired
    private CasProperties casProperties;

    @Autowired
    private AppProperties appProperties;

    @GetMapping("/logout")
    public String logout() {
        // cas服务器端登出
        return "redirect:" + casProperties.getLogoutUrl() + "?service=" + appProperties.getHost() + LOGOUTED_URL;
    }

    @GetMapping(LOGOUTED_URL)
    public String logouted() {
        // 客户端shiro登出
        SecurityUtils.getSubject().logout();
        return "redirect:/index";
    }

}
