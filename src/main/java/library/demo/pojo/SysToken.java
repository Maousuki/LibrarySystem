package library.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysToken {

    private long userId;
    private String token;
    private LocalDateTime expireTime;
    private LocalDateTime updateTime;
}
