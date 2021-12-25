package library.demo.auth;

import library.demo.pojo.User;
import library.demo.serviceImpl.ShiroServiceImpl;
import library.demo.serviceImpl.UserServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    ShiroServiceImpl shiroService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1. 从 PrincipalCollection 中来获取登录用户的信息
        User user = (User) principals.getPrimaryPrincipal();
        //2.添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole(user.getRole()+"");
        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取token，既前端传入的token
        String accessToken = (String) token.getPrincipal();
        //1. 根据accessToken，查询用户信息
        String userId;
        User user;
        try {
            userId = (String) redisTemplate.opsForValue().get(accessToken);
            //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
            user = userService.queryUserById(Long.valueOf(userId));
            //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
            if (user == null) {
                throw new UnknownAccountException("用户不存在!");
            }
        } catch (Exception e) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }
        //5. 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为: SimpleAuthenticationInfo
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, this.getName());
        return info;
    }
}
