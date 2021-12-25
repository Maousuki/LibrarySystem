package library.demo.serviceImpl;

import library.demo.dao.BookMapper;
import library.demo.pojo.Book;
import library.demo.pojo.Record;
import library.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookMapper bookMapper;

    @Override
    public int addBook(Book book) {
        return bookMapper.addBook(book);
    }

    @Override
    public int deleteBook(long book_id) {
        return bookMapper.deleteBook(book_id);
    }

    @Override
    public int updateBook(Book book) {
        return bookMapper.updateBook(book);
    }

    @Override
    public List<Book> queryAllBooks() {
        return bookMapper.queryAllBooks();
    }

    @Override
    public Book queryBookById(long book_id) {
        return bookMapper.queryBookById(book_id);
    }

    @Override
    public Record queryRecordByBookId(long book_id) {
        return bookMapper.queryRecordByBookId(book_id);
    }

    @Override
    public List<Record> queryBookAllRecords(long book_id) {
        return bookMapper.queryBookAllRecords(book_id);
    }

    @Override
    public List<Record> queryUserRecords(long user_id) {
        return bookMapper.queryUserRecords(user_id);
    }

    @Override
    public Record queryRecordById(long record_id) {
        return bookMapper.queryRecordById(record_id);
    }

    @Override
    public int borrowBook(Record record) {
        return bookMapper.borrowBook(record);
    }

    @Override
    public int updateStatus(long book_id, int status) {
        return bookMapper.updateStatus(book_id,status);
    }

    @Override
    public int updateReturnDate(long record_id, LocalDateTime date) {
        return bookMapper.updateReturnDate(record_id,date);
    }

    @Override
    public List<Record> queryAllRecords() {
        return bookMapper.queryAllRecords();
    }
}
