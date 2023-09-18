package example.Advanced.Music.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDto {
    private long id;
    private String name;
    private long creatorId;
    private boolean isFavorite;
    private List<SongDto> songDtoList;
}
