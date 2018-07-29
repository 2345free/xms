package com.xiao.xms.realm;

import com.xiao.xms.mapper.UserMapper;
import com.xiao.xms.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author luoxiaoxiao
 */
@Slf4j
public class MyShiroRealm extends CasRealm {

    @Autowired
    private UserMapper userMapper;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("##################执行Shiro权限认证##################");
        return null;
    }

    /**
     * 1.CAS认证,验证用户身份
     * 2.将用户基本信息设置到会话中
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        AuthenticationInfo authc = super.doGetAuthenticationInfo(token);
        String account = (String) authc.getPrincipals().getPrimaryPrincipal();
        User var1 = new User();
        var1.setUsername(account);
        User user = userMapper.selectOne(var1);
        if (user != null) {
            //将用户信息存入session中
            SecurityUtils.getSubject().getSession().setAttribute("user", user);
        }
        return authc;
    }
}
