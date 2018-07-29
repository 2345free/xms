package com.xiao.xms;

import com.xiao.xms.mapper.MyMapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author: luoxiaoxiao
 */
@Configuration
@ComponentScan(basePackages = {"com.xiao.xms"})
@MapperScan(basePackages = {"com.xiao.xms.mapper"}, markerInterface = MyMapper.class)
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/index.html");
    }
}
