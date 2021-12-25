package library.demo.service;

public interface ShiroService {

    //创建Token
    String createToken(long userId);

    //登出
    void logout(String token);

}
