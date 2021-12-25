package library.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import library.demo.pojo.Book;
import library.demo.pojo.Record;
import library.demo.pojo.User;
import library.demo.serviceImpl.BookServiceImpl;
import library.demo.serviceImpl.UserServiceImpl;
import library.demo.utils.IdUtil;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BookController {

    @Autowired
    BookServiceImpl bookService;
    @Autowired
    UserServiceImpl userService;

    DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //创建书目
    @RequiresRoles({"1"})
    @PostMapping("/api/book")
    public JSONObject addBook(@RequestBody String str) {
        JSONObject jsonReq = JSONObject.parseObject(str);
        Book book = jsonReq.toJavaObject(Book.class);
        book.setId(IdUtil.getRandomId());
        JSONObject jsonRes = new JSONObject();
        if (bookService.addBook(book) != 0) {
            jsonRes.put("code", 0);
            jsonRes.put("msg", "ok");
            JSONObject data = new JSONObject();
            data.put("book_id", book.getId());
            jsonRes.put("data", data);
        }
        return jsonRes;
    }

    //删除书目
    @RequiresRoles("1")
    @DeleteMapping("/api/book")
    public JSONObject deleteBook(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String book_id = jsonReq.getString("book_id");

        JSONObject jsonRes = new JSONObject();
        JSONObject data = new JSONObject();

        Book book = bookService.queryBookById(Long.parseLong(book_id));

        if (book == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","书目不存在");
            data.put("name","null");
            data.put("place","null");
            data.put("introduction","null");
            data.put("author","null");
            data.put("price","null");
            jsonRes.put("data",data);
        } else {
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
            data.put("name",book.getName());
            data.put("place",book.getPlace());
            data.put("introduction",book.getIntroduction());
            data.put("author",book.getAuthor());
            data.put("price",book.getPrice());
            jsonRes.put("data",data);
            bookService.deleteBook(book.getId());
        }

        return jsonRes;
    }

    //更新书目
    @RequiresRoles("1")
    @PutMapping("/api/book")
    public JSONObject updateBookInfo(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        Book book = jsonReq.toJavaObject(Book.class);
        String book_id = jsonReq.getString("book_id");
        book.setId(Long.parseLong(book_id));

        JSONObject jsonRes = new JSONObject();
        JSONObject data = new JSONObject();

        Book bookById = bookService.queryBookById(Long.parseLong(book_id));

        if (bookById == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","书目不存在");
            data.put("name","null");
            data.put("place","null");
            data.put("introduction","null");
            data.put("author","null");
            data.put("price","null");
            jsonRes.put("data",data);
        } else {
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
            data.put("name",book.getName());
            data.put("place",book.getPlace());
            data.put("introduction",book.getIntroduction());
            data.put("author",book.getAuthor());
            data.put("price",book.getPrice());
            jsonRes.put("data",data);
            bookService.updateBook(book);
        }

        return jsonRes;
    }

    //获得书目信息
    @GetMapping("/api/book")
    public JSONObject getBookInfo(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String book_id = jsonReq.getString("book_id");

        Book book = bookService.queryBookById(Long.parseLong(book_id));

        JSONObject jsonRes = new JSONObject();
        JSONObject data = new JSONObject();

        if (book == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","书目不存在");
            data.put("name","null");
            data.put("place","null");
            data.put("introduction","null");
            data.put("author","null");
            data.put("price","null");
            jsonRes.put("data",data);
        } else {
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
            data.put("name",book.getName());
            data.put("place",book.getPlace());
            data.put("introduction",book.getIntroduction());
            data.put("author",book.getAuthor());
            data.put("price",book.getPrice());
            jsonRes.put("data",data);
        }

        return jsonRes;
    }

    //查询书目列表
    @GetMapping("/api/books")
    public JSONObject getBookList(){
        List<Book> books = bookService.queryAllBooks();
        JSONArray list = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        jsonRes.put("code",0);
        jsonRes.put("msg","ok");

        Map<String,Object> map = new HashMap<>();

        for (Book book : books) {
            map.put("book_id",book.getId());
            map.put("name",book.getName());
            list.add(map);
            map = new HashMap<>();
        }
        jsonRes.put("data",list);

        return jsonRes;
    }

    //借书
    @PostMapping("/api/book/borrow")
    public JSONObject borrowBook(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String book_id = jsonReq.getString("book_id");
        String user_id = jsonReq.getString("user_id");

        Record record = new Record();
        record.setId(IdUtil.getRandomId());
        record.setBookId(Long.parseLong(book_id));
        record.setUserId(Long.parseLong(user_id));

        JSONObject jsonRes = new JSONObject();
        JSONObject data = new JSONObject();

        Book book = bookService.queryBookById(Long.parseLong(book_id));
        User user = userService.queryUserById(Long.parseLong(user_id));

        if (book == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","书目不存在");
            data.put("record_id","null");
            data.put("borrow_date","null");
            jsonRes.put("data",data);
        } else if (user == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","用户不存在");
            data.put("record_id","null");
            data.put("borrow_date","null");
            jsonRes.put("data",data);
        } else if (book.getStatus() == 1){
            jsonRes.put("code",1);
            jsonRes.put("msg","书被借出");
            data.put("record_id","null");
            data.put("borrow_date","null");
            jsonRes.put("data",data);
        } else {
            record.setBorrowDate(LocalDateTime.now());
            bookService.updateStatus(Long.parseLong(book_id),1);
            bookService.borrowBook(record);
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
            data.put("record_id",record.getId());
            data.put("borrow_date",dft.format(record.getBorrowDate()));
            jsonRes.put("data",data);
        }

        return jsonRes;
    }

    //还书
    @PostMapping("/api/book/return")
    public JSONObject returnBook(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String record_id = jsonReq.getString("record_id");
        String user_id = jsonReq.getString("user_id");

        User user = userService.queryUserById(Long.parseLong(user_id));
        Record record = bookService.queryRecordById(Long.parseLong(record_id));

        JSONObject jsonRes = new JSONObject();
        JSONObject data = new JSONObject();

        if (user == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","用户不存在");
            data.put("borrow_date","null");
            data.put("return_date","null");
            jsonRes.put("data",data);
        } else if (record == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","借书记录不存在");
            data.put("borrow_date","null");
            data.put("return_date","null");
            jsonRes.put("data",data);
        } else {
            LocalDateTime now = LocalDateTime.now();
            bookService.updateReturnDate(Long.parseLong(record_id),now);
            bookService.updateStatus(record.getBookId(),0);
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
            data.put("borrow_date",dft.format(record.getBorrowDate()));
            data.put("return_date",dft.format(now));
            jsonRes.put("data",data);
        }

        return jsonRes;
    }

    //查询指定书目的借还状态
    @GetMapping("/api/book/status")
    public JSONObject getBookStatus(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String book_id = jsonReq.getString("book_id");

        Book book = bookService.queryBookById(Long.parseLong(book_id));

        JSONObject jsonRes = new JSONObject();
        JSONObject data = new JSONObject();

        if (book == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","书目不存在");
            data.put("user_id","null");
            data.put("borrow_date","null");
            data.put("return_date","null");
        } else {
            List<Record> records = bookService.queryBookAllRecords(Long.parseLong(book_id));
            if (records.size() == 0){
                jsonRes.put("code",0);
                jsonRes.put("msg","ok");
                data.put("user_id","null");
                data.put("borrow_date","null");
                data.put("return_date","null");
                jsonRes.put("data",data);
                return jsonRes;
            }
            Record record = records.get(records.size() - 1);
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
            data.put("user_id",record.getUserId());
            data.put("borrow_date",dft.format(record.getBorrowDate()));
            if (record.getReturnDate() == null){
                data.put("return_date","null");
            } else {
                data.put("return_date",dft.format(record.getReturnDate()));
            }
        }
        jsonRes.put("data",data);
        return jsonRes;
    }

    //查询指定书目的所有借还记录
    @RequiresRoles("1")
    @GetMapping("/api/book/records")
    public JSONObject getBookRecords(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String book_id = jsonReq.getString("book_id");

        Book book = bookService.queryBookById(Long.parseLong(book_id));

        JSONObject jsonRes = new JSONObject();
        JSONArray list = new JSONArray();
        Map<String,Object> map = new HashMap<>();

        if (book == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","书目不存在");
            jsonRes.put("data",list);
        } else {
            List<Record> records = bookService.queryBookAllRecords(Long.parseLong(book_id));
            for (Record record : records) {
                map.put("record_id",record.getId());
                map.put("user_id",record.getUserId());
                map.put("username",userService.queryUserById(record.getUserId()).getUsername());
                map.put("borrow_date",dft.format(record.getBorrowDate()));
                if (record.getReturnDate() == null){
                    map.put("return_date","null");
                } else {
                    map.put("return_date",dft.format(record.getReturnDate()));
                }
                list.add(map);
                map = new HashMap<>();
            }
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
            jsonRes.put("data",list);
        }

        return jsonRes;
    }

    //查询指定用户的所有借书记录
    @RequiresRoles("1")
    @GetMapping("/api/user/books")
    public JSONObject getUserRecords(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String user_id = jsonReq.getString("user_id");

        User user = userService.queryUserById(Long.parseLong(user_id));

        JSONObject jsonRes = new JSONObject();
        JSONArray list = new JSONArray();
        Map<String,Object> map = new HashMap<>();

        if (user == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","用户不存在");
            jsonRes.put("data",list);
        } else {
            List<Record> records = bookService.queryUserRecords(Long.parseLong(user_id));
            for (Record record : records) {
                map.put("record_id",record.getId());
                map.put("book_id",record.getBookId());
                map.put("name",bookService.queryBookById(record.getBookId()).getName());
                map.put("borrow_date",dft.format(record.getBorrowDate()));
                if (record.getReturnDate() == null){
                    map.put("return_date","null");
                } else {
                    map.put("return_date",dft.format(record.getReturnDate()));
                }
                list.add(map);
                map = new HashMap<>();
            }
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
            jsonRes.put("data",list);
        }

        return jsonRes;
    }

    //通过记录查询书目借还状态
    @RequiresRoles("1")
    @GetMapping("/api/user/record")
    public JSONObject getStatusByRid(@RequestBody String str){
        JSONObject jsonReq = JSONObject.parseObject(str);
        String record_id = jsonReq.getString("record_id");

        Record record = bookService.queryRecordById(Long.parseLong(record_id));

        JSONObject jsonRes = new JSONObject();
        JSONObject data = new JSONObject();

        if (record == null){
            jsonRes.put("code",1);
            jsonRes.put("msg","记录不存在");
            data.put("book_id","null");
            data.put("user_id","null");
            data.put("borrow_date","null");
            data.put("return_date","null");
        } else {
            jsonRes.put("code",0);
            jsonRes.put("msg","ok");
            data.put("book_id",record.getBookId());
            data.put("user_id",record.getUserId());
            data.put("borrow_date",dft.format(record.getBorrowDate()));
            if (record.getReturnDate() == null){
                data.put("return_date","null");
            } else {
                data.put("return_date",dft.format(record.getReturnDate()));
            }
        }
        jsonRes.put("data",data);

        return jsonRes;
    }

    //查询所有借还记录
    @RequiresRoles("1")
    @GetMapping("/api/records")
    public JSONObject getAllRecords() {
        JSONObject jsonRes = new JSONObject();
        JSONArray list = new JSONArray();
        Map<String,Object> map = new HashMap<>();
        List<Record> records = bookService.queryAllRecords();

        jsonRes.put("code",0);
        jsonRes.put("msg","ok");

        for (Record record : records) {
            map.put("record_id",record.getId());
            map.put("book_id",record.getBookId());
            map.put("usr_id",record.getUserId());
            map.put("borrow_date",dft.format(record.getBorrowDate()));
            if (record.getReturnDate() == null){
                map.put("return_date","null");
            } else {
                map.put("return_date",dft.format(record.getReturnDate()));
            }
            list.add(map);
            map = new HashMap<>();
        }
        jsonRes.put("data", list);

        return jsonRes;
    }

}
