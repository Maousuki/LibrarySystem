package library.demo.serviceImpl;

import library.demo.auth.TokenGenerator;
import library.demo.dao.ShiroMapper;
import library.demo.dao.UserMapper;
import library.demo.pojo.SysToken;
import library.demo.service.ShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShiroServiceImpl implements ShiroService {

    //12小时后失效
    private final static int EXPIRE = 12;

    @Autowired
    UserMapper userMapper;
    @Autowired
    ShiroMapper shiroMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> createToken(long userId) {
        Map<String, Object> result = new HashMap<>();
        //生成一个token
        String token = TokenGenerator.generateValue();
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        //过期时间
        LocalDateTime expireTime = now.plusHours(EXPIRE);
        //判断是否生成过token
        SysToken tokenEntity = shiroMapper.findByUserId(userId);
        if (tokenEntity == null) {
            tokenEntity = new SysToken();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            shiroMapper.save(tokenEntity);
        } else {
            shiroMapper.update(userId,token,expireTime,now);
        }

        result.put("token", token);
        result.put("expire", expireTime);
        return result;
    }

    @Override
    public void logout(String token) {
        SysToken byToken = findByToken(token);
        long userId = byToken.getUserId();
        //删除token
        shiroMapper.delete(userId);
    }

    @Override
    public SysToken findByToken(String accessToken) {
        return shiroMapper.findByToken(accessToken);
    }

}
