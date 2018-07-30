/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.xiao.xms.config;


import com.xiao.xms.realm.MyShiroRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author luoxiaoxiao
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({CasProperties.class, AppProperties.class})
public class ShiroCasConfig {

    @Autowired
    private CasProperties casProperties;

    @Autowired
    private AppProperties appProperties;

    @Bean
    public DefaultWebSecurityManager securityManager(MyShiroRealm myShiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm);
        // 用户授权/认证信息Cache, 采用EhCache 缓存
        securityManager.setCacheManager(ehCacheManager());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public MyShiroRealm myShiroRealm(EhCacheManager cacheManager) {
        MyShiroRealm realm = new MyShiroRealm();
        realm.setCacheManager(cacheManager);
        realm.setCasServerUrlPrefix(casProperties.getHost());
        // 客户端回调地址
        realm.setCasService(appProperties.getHost() + appProperties.getLoginCallback());
        return realm;
    }

    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
        return cacheManager;
    }

    /**
     * 注册单点登出listener
     */
    @Bean
    public ServletListenerRegistrationBean singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
        bean.setListener(new SingleSignOutHttpSessionListener());
        //bean.setOrder(Ordered.HIGHEST_PRECEDENCE); //设置优先级
        return bean;
    }

    /**
     * 注册单点登出filter
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new SingleSignOutFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }

    /**
     * CAS过滤器配置
     */
    @Bean
    public CasFilter casFilter() {
        CasFilter casFilter = new CasFilter();
        // 登录失败后跳转的URL，也就是 Shiro 执行 CasRealm 的 doGetAuthenticationInfo
        // 方法向CasServer验证tiket
        // 我们选择认证失败后再打开首页
        casFilter.setFailureUrl(appProperties.getLoginUrl());
        return casFilter;
    }

    /**
     * 注册DelegatingFilterProxy（Shiro）
     * 集成Shiro有2种方法：
     * 1.按这个方法自己组装一个FilterRegistrationBean
     * （这种方法更为灵活，可以自己定义UrlPattern
     * 在项目使用中你可能会因为一些很但疼的问题最后采用它,
     * 想使用它你可能需要看官网或者已经很了解Shiro的处理原理了）
     * 2.直接使用ShiroFilterFactoryBean
     * （这种方法比较简单，其内部对ShiroFilter做了组装工作,
     * 无法自己定义UrlPattern默认拦截
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        // 该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "false");
        filterRegistration.setEnabled(true);
        // 可以自己灵活的定义很多，避免一些根本不需要被Shiro处理的请求被包含进来
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    /**
     * Shiro过滤器配置
     * 我们在这里可以获取到相关访问数据库的对象,然后读取数据库相关配置,
     * 配置到 shiroFilterFactoryBean的访问规则中.
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl(casProperties.getLoginUrl() + "?service=" + appProperties.getHost() + appProperties.getLoginCallback());
        // 登录成功后要跳转的连接
        shiroFilterFactoryBean.setSuccessUrl("/");
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/403.html");

        /**
         * 加载shiroFilter权限控制规则（从数据库读取然后配置）
         */
        /////////////////////// 下面这些规则配置最好配置到配置文件中 ///////////////////////
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // auth: 该过滤器下的页面必须验证后才能访问,
        // 它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        // 这里为了测试,只限制/index，实际开发中请修改为具体拦截的请求规则
        filterChainDefinitionMap.put("/index", "authc");
        log.info("##################从数据库读取权限规则，加载到shiroFilter中##################");
        // 这里是测试固定写死的值,也可以从数据库或其他配置中读取
        filterChainDefinitionMap.put("/user/edit/**", "authc,perms[user:edit]");
        /**
         * @非常重要: 登录成功后解析获取ticket的过滤器
         */
        filterChainDefinitionMap.put(appProperties.getLoginCallback(), "casFilter");
        // anon: 它对应的过滤器里面是空的,什么都没做,可以理解为不拦截
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * shiro组件申明周期管理
     *
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启自动代理
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启权限注解支持
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor attributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        attributeSourceAdvisor.setSecurityManager(securityManager);
        return attributeSourceAdvisor;
    }

    /******************************** session管理 ******************************/
    @Bean
    public WebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        return sessionManager;
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie cookie = new SimpleCookie();
        cookie.setName("mySessionId");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        return cookie;
    }

    @Bean
    public EnterpriseCacheSessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        sessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        return sessionDAO;
    }

}
