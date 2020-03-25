package com.example.manageSystem.admin.module.shiro;

import com.alibaba.fastjson.JSON;
import com.example.manageSystem.admin.model.Menu;
import com.example.manageSystem.admin.model.Role;
import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.menu.service.MenuService;
import com.example.manageSystem.admin.module.role.service.RoleService;
import com.example.manageSystem.admin.module.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


public class ShiroRealm extends AuthorizingRealm {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("授权doGetAuthorizationInfo+"+principalCollection.toString());
        User user = userService.findByUserName((String) principalCollection.getPrimaryPrincipal());


        //把principals放session中 key=userId value=principals
        SecurityUtils.getSubject().getSession().setAttribute(String.valueOf(user.getUserId()),SecurityUtils.getSubject().getPrincipals());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //赋予角色
        List<Role> roles = roleService.findByUserId(user.getUserId());
        for(Role role : roles){
            simpleAuthorizationInfo.addRole(role.getRoleName());

            //赋予权限
            List<Menu> menus = menuService.findByRoleId(role.getRoleId());
            for(Menu menu : menus){
                if(StringUtils.isNotBlank(menu.getPerms())){
                    simpleAuthorizationInfo.addStringPermission(menu.getPerms());
                }
            }
        }


        //设置登录次数、时间
//        userService.updateUserLogin(user);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("认证doGetAuthenticationInfo:"  + authenticationToken.toString());

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName=token.getUsername();
        logger.info("当前用户用户名和密码："+userName+","+ JSON.toJSONString(token.getPassword()));

        User user = userService.findByUserName(token.getUsername());
        if (user != null) {
//            byte[] salt = Encodes.decodeHex(user.getSalt());
//            ShiroUser shiroUser=new ShiroUser(user.getId(), user.getLoginName(), user.getName());
            //设置用户session
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("user", user);
            return new SimpleAuthenticationInfo(userName,user.getPassword(),getName());
        } else {
            return null;
        }
//        return null;
    }

}
