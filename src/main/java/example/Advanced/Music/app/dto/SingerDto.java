package example.Advanced.Music.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingerDto {
    private long id;
    private String name;
    private int songCount;
}
