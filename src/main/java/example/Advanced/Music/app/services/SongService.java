package example.Advanced.Music.app.services;

import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

public interface SongService {
    Page<SongDto> findAll(Pageable pageable) throws Exception;
    SongDto findById(long id) throws Exception;
    List<SongDto> findByIds(List<Long> ids) throws Exception;
    String deleteById(long id) throws Exception;
    SongDto createSong(@Valid String nameSong, MultipartFile imageFile, MultipartFile songFile, List<String> nameSingers) throws Exception;
    SongDto update(long id, @Valid UpdateSongRequest request) throws Exception;
    SongDto changeThumbnail(long id, MultipartFile imageFile) throws Exception;
    SongDto changeLyric(long id, String lyric) throws Exception;
    SongDto listenedSong(long songId) throws Exception;

}
