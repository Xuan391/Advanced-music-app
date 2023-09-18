package example.Advanced.Music.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListPlayListDto {
    private long id;
    private String name;
    private long creatorId;
    private String creatorName;
}
