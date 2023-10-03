package example.Advanced.Music.app.dto;

import example.Advanced.Music.app.entities.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SingerDetailDto {
    private String singerName;
    private List<SongDto> songs;
}
