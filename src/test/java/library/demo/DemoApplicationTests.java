package library.demo;

import library.demo.pojo.Book;
import library.demo.utils.Md5SaltUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        System.out.println(Md5SaltUtil.encrypt("李四","222222"));
    }

    @Test
    public void redisTest(){
        Long id = 1640003394552L;
        redisTemplate.opsForValue().set(id+"",123);
        System.out.println(redisTemplate.opsForValue().get(id+""));

    }

}
