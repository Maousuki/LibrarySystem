package library.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import library.demo.pojo.Book;
import library.demo.pojo.User;
import library.demo.serviceImpl.ShiroServiceImpl;
import library.demo.serviceImpl.UserServiceImpl;
import library.demo.utils.Md5SaltUtil;
import library.demo.utils.IdUtil;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    ShiroServiceImpl shiroService;

    //用户注册
    @PostMapping("/api/user")
    public JSONObject addUser(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String username = jsonReq.getString("username");
        String password = jsonReq.getString("password");
        String email = jsonReq.getString("email");

        JSONObject jsonRes = new JSONObject();
        JSONObject data = new JSONObject();

        if (userService.queryUserByName(username) != null){
            jsonRes.put("code",1);
            jsonRes.put("msg","用户名已注册");
            data.put("user_id","null");
            jsonRes.put("data",data);
        } else if (userService.queryUserByEmail(email) != null){
            jsonRes.put("code",1);
            jsonRes.put("msg","邮箱已注册");
            data.put("user_id","null");
            jsonRes.put("data",data);
        } else {
            User user = jsonReq.toJavaObject(User.class);
            String lastPassword = Md5SaltUtil.encrypt(username, password);
            user.setId(IdUtil.getRandomId());
            user.setPassword(lastPassword);
            userService.addUser(user);
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
            data.put("user_id",user.getId());
            jsonRes.put("data",data);
        }
        return jsonRes;
    }

    //用户登录
    @PostMapping("/api/login")
    public JSONObject login(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String username = jsonReq.getString("username");
        String password = jsonReq.getString("password");
        JSONObject jsonRes = new JSONObject();
        JSONObject data = new JSONObject();

        User user = userService.queryUserByName(username);

        if (user == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","用户名不存在");
            data.put("user_id","null");
        } else if(!user.getPassword().equals(Md5SaltUtil.encrypt(username,password))){
            jsonRes.put("code",1);
            jsonRes.put("msg","密码错误");
            data.put("user_id","null");
        } else {
            Map<String, Object> result = shiroService.createToken(user.getId());
            String token = (String) result.get("token");
            jsonRes.put("code","0");
            jsonRes.put("msg","登录成功");
            jsonRes.put("access_token",token);
            data.put("user_id",user.getId());
        }
        jsonRes.put("data",data);
        return jsonRes;
    }

    //用户登出
    @PostMapping("/api/logout")
    public JSONObject logout(@RequestHeader("access_token") String token){
        JSONObject jsonRes = new JSONObject();
        shiroService.logout(token);
        jsonRes.put("code",0);
        jsonRes.put("msg","退出登录成功！");
        return jsonRes;
    }

    //获取用户信息
    @GetMapping("/api/user")
    public JSONObject getUser(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String user_id = jsonReq.getString("user_id");
        User user = userService.queryUserById(Long.parseLong(user_id));

        JSONObject jsonRes = new JSONObject();
        JSONObject data = new JSONObject();

        if (user == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","该用户不存在");
            data.put("username","null");
            data.put("nickname","null");
            data.put("email","null");
        } else {
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
            data.put("username",user.getUsername());
            data.put("nickname",user.getNickname());
            data.put("email",user.getEmail());
        }
        jsonRes.put("data",data);
        return jsonRes;
    }

    //更改用户信息
    @PutMapping("/api/user")
    public JSONObject updateUserInfo(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String email = jsonReq.getString("email");
        String nickname = jsonReq.getString("nickname");
        String user_id = jsonReq.getString("user_id");

        JSONObject jsonRes = new JSONObject();

        User user = userService.queryUserByEmail(email);

        if (user != null){
            jsonRes.put("code",1);
            jsonRes.put("msg","邮箱已被占用");
        } else {
            userService.updateInfo(email,nickname,Long.parseLong(user_id));
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
        }
        return jsonRes;
    }

    //修改用户密码
    @PostMapping("/api/user/password")
    public JSONObject updatePassword(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String user_id = jsonReq.getString("user_id");
        String password = jsonReq.getString("password");
        String old_password = jsonReq.getString("old_password");

        JSONObject jsonRes = new JSONObject();

        User user = userService.queryUserById(Long.parseLong(user_id));

        if (!user.getPassword().equals(Md5SaltUtil.encrypt(user.getUsername(),old_password))){
            jsonRes.put("code",1);
            jsonRes.put("msg","原始密码错误");
        } else {
            userService.updatePassword(Long.parseLong(user_id),Md5SaltUtil.encrypt(user.getUsername(),password));
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
        }

        return jsonRes;
    }

    //获取用户列表
    @RequiresRoles("1")
    @GetMapping("/api/users")
    public JSONObject getUserList(){
        List<User> users = userService.queryAllUsers();

        JSONObject jsonRes = new JSONObject();
        JSONArray list = new JSONArray();
        jsonRes.put("code",0);
        jsonRes.put("msg","ok");

        Map<String,Object> map = new HashMap<>();

        for (User user : users) {
            map.put("user_id",user.getId());
            map.put("username",user.getUsername());
            list.add(map);
            map = new HashMap<>();
        }
        jsonRes.put("data",list);

        return jsonRes;
    }
}
