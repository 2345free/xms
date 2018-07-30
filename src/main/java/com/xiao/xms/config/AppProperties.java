package com.xiao.xms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: luoxiaoxiao
 * @date: 2018-07-29 13:38
 */
@Data
@ConfigurationProperties(prefix = "app.server")
public class AppProperties {

    private String host;

    private String loginUrl;

    private String logoutUrl;

    /**
     * cas登陆成功后回掉接受认证凭证的接口
     */
    private String loginCallback;

}
