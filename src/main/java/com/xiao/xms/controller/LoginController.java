package com.xiao.xms.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author: luoxiaoxiao
 */
@Slf4j
@Controller
public class LoginController {

    @GetMapping({"/logined"})
    public String index(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            log.info("{}={}", paramName, request.getParameter(paramName));
        }
        return "index";
    }

    @GetMapping(value = "/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        // 使用权限管理工具进行用户的退出,跳出登录,给出提示信息
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/index";
    }

}
