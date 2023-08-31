package example.Advanced.Music.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowSongOfUserDto {
    private long idUser;
    private String nameUser;
    private List<SongDto> songDtos;
}
