package library.demo.dao;

import library.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    //用户注册
    int addUser(User user);

    //修改用户密码
    int updatePassword(@Param("id") long id, @Param("newPassword") String newPassword);

    //客户端用户登录
    User loginUser(@Param("id") long id, @Param("password") String password);

    //更改用户信息
    int updateInfo(@Param("email") String email, @Param("nickname") String nickname, @Param("id") long id);

    //获取用户信息
    User queryUserById(@Param("id") long user_id);

    //获取用户列表
    List<User> queryAllUsers();

    //根据邮箱查找用户
    User queryUserByEmail(@Param("email") String email);

    //根据用户名查找用户
    User queryUserByName(@Param("name") String name);
}
