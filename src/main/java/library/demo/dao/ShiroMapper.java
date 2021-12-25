package library.demo.dao;

import library.demo.pojo.SysToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Mapper
@Repository
public interface ShiroMapper {

    //通过token查找
    SysToken findByToken(@Param("token") String token);

    //通过userID查找
    SysToken findByUserId(@Param("userId") long userId);

    //存Token
    int save(SysToken token);

    //删除Token
    int delete(@Param("userId") long userId);

    //更新Token
    int update(@Param("userId") long userId, @Param("token") String token,
               @Param("expire") LocalDateTime expireTime,
               @Param("update") LocalDateTime updateTime);
}
