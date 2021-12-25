package library.demo.serviceImpl;

import library.demo.dao.UserMapper;
import library.demo.pojo.User;
import library.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public int addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public int updatePassword(long id, String newPassword) {
        return userMapper.updatePassword(id, newPassword);
    }

    @Override
    public User loginUser(long id, String password) {
        return userMapper.loginUser(id, password);
    }

    @Override
    public int updateInfo(String email, String nickname, long id) {
        return userMapper.updateInfo(email,nickname,id);
    }

    @Override
    public User queryUserById(long user_id) {
        return userMapper.queryUserById(user_id);
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.queryAllUsers();
    }

    @Override
    public User queryUserByEmail(String email) {
        return userMapper.queryUserByEmail(email);
    }

    @Override
    public User queryUserByName(String name) {
        return userMapper.queryUserByName(name);
    }
}
