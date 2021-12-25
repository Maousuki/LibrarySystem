package library.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {

    private long Id;
    private long BookId;
    private long UserId;
    private LocalDateTime BorrowDate;
    private LocalDateTime ReturnDate;

}
