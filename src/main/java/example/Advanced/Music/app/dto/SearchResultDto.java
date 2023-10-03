package example.Advanced.Music.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResultDto {
    private List<ListPlayListDto> playlists;
    private List<SongDto> songs;
    private List<SingerDto> singers;
    private List<UserDto> users;

}
