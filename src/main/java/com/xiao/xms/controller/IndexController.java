package com.xiao.xms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: luoxiaoxiao
 * @date: 2018-07-25 18:45
 */
@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

}
