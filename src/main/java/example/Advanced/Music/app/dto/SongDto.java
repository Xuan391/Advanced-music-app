package example.Advanced.Music.app.dto;

import example.Advanced.Music.app.entities.Singer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SongDto {
    private long id;
    private String name;
    private List<Singer> singers;
    private String songUrl;
    private String thumbnailUrl;
    private long creatorId;
    private String nameCreator;
    private int downloadCount;
    private int listenedCount;
}
