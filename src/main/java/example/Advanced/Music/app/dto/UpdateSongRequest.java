package example.Advanced.Music.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateSongRequest {
    private String nameSong;
    private List<String> listNameSinger;
}
