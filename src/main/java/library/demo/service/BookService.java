package library.demo.service;

import library.demo.pojo.Book;
import library.demo.pojo.Record;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookService {
    //创建书目
    int addBook(Book book);

    //删除书目
    int deleteBook(@Param("id") long book_id);

    //更新书目
    int updateBook(Book book);

    //获取书目列表
    List<Book> queryAllBooks();

    //获取书目信息
    Book queryBookById(long book_id);

    //查询指定书目的借还状态
    Record queryRecordByBookId(long book_id);

    //查询指定书目的所有借还记录
    List<Record> queryBookAllRecords(long book_id);

    //查询指定用户的所有借书记录
    List<Record> queryUserRecords(long user_id);

    //通过记录查询书目借还状态
    Record queryRecordById(long record_id);

    //借书
    int borrowBook(Record record);

    //更新状态
    int updateStatus(long book_id, int status);

    //更新还书日期
    int updateReturnDate(long record_id, LocalDateTime date);

    //查询所有借还记录
    List<Record> queryAllRecords();
}
