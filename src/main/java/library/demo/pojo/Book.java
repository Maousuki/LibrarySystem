package library.demo.pojo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private long Id;
    private String Name;
    private String Place;
    private String Introduction;
    private String Author;
    private float Price;
    private int Status; //0 未借出 1 借出

}
