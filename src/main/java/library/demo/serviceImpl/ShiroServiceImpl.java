package library.demo.serviceImpl;

import library.demo.auth.TokenGenerator;
import library.demo.dao.UserMapper;
import library.demo.service.ShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class ShiroServiceImpl implements ShiroService {

    //12小时后失效
    private final static int EXPIRE = 12;

    @Autowired
    UserMapper userMapper;
    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;

    @Override
    public String createToken(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //判断是否生成过token
        String getToken = (String) redisTemplate.opsForValue().get(String.valueOf(userId));
        if (Objects.isNull(getToken)){
            redisTemplate.opsForValue().set(String.valueOf(userId),token,EXPIRE,TimeUnit.HOURS);
            redisTemplate.opsForValue().set(token,String.valueOf(userId),EXPIRE,TimeUnit.HOURS);
            return token;
        } else {
            redisTemplate.expire(String.valueOf(userId),EXPIRE,TimeUnit.HOURS);
            redisTemplate.expire(getToken,EXPIRE,TimeUnit.HOURS);
            return getToken;
        }

    }

    @Override
    public void logout(String token) {
        Object getUserId = redisTemplate.opsForValue().get(token);
        redisTemplate.delete(getUserId);
        redisTemplate.delete(token);
    }
}
