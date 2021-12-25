package library.demo;

import library.demo.utils.Md5SaltUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(Md5SaltUtil.encrypt("李四","222222"));
    }

}
