package library.demo.dao;

import library.demo.pojo.Book;
import library.demo.pojo.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@Repository
public interface BookMapper {

    //创建书目
    int addBook(Book book);

    //删除书目
    int deleteBook(@Param("id") long id);

    //更新书目
    int updateBook(Book book);

    //获取书目列表
    List<Book> queryAllBooks();

    //获取书目信息
    Book queryBookById(@Param("id") long book_id);

    //查询指定书目的借还状态
    Record queryRecordByBookId(@Param("id") long book_id);

    //查询指定书目的所有借还记录
    List<Record> queryBookAllRecords(@Param("id") long book_id);

    //查询指定用户的所有借书记录
    List<Record> queryUserRecords(@Param("uid") long user_id);

    //通过记录查询书目借还状态
    Record queryRecordById(@Param("rid") long record_id);

    //借书
    int borrowBook(Record record);

    //更新状态
    int updateStatus(@Param("id") long book_id, @Param("status") int status);

    //更新还书日期
    int updateReturnDate(@Param("id") long record_id, @Param("date") LocalDateTime date);

    //查询所有借还记录
    List<Record> queryAllRecords();
}
