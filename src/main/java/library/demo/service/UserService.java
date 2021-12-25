package library.demo.service;

import library.demo.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    //用户注册
    int addUser(User user);

    //修改用户密码
    int updatePassword(long id, String newPassword);

    //客户端用户登录
    User loginUser(long id, String password);

    //更改用户信息
    int updateInfo(String email, String nickname, long id);

    //获取用户信息
    User queryUserById(long user_id);

    //获取用户列表
    List<User> queryAllUsers();

    //根据邮箱查找用户
    User queryUserByEmail(@Param("email") String email);

    //根据用户名查找用户
    User queryUserByName(@Param("name") String name);
}
