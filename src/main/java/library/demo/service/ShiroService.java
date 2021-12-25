package library.demo.service;

import library.demo.pojo.SysToken;
import library.demo.pojo.User;

import java.util.Map;

public interface ShiroService {

    //创建Token
    Map<String,Object> createToken(long userId);

    //登出
    void logout(String token);

    //通过token查token
    SysToken findByToken(String accessToken);

}
