package library.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long Id;
    private String Username;
    private String Password;
    private String Email;
    private String Nickname;
    private int Role;

}
