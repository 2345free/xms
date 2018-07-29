package com.xiao.xms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author luoxiaoxiao
 */
@Data
@ConfigurationProperties(prefix = "cas.server")
public class CasProperties {

    private String host;

    private String loginUrl;

    private String logoutUrl;

}
