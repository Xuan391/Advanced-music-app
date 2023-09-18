package example.Advanced.Music.app.services;

import example.Advanced.Music.app.dto.ListPlayListDto;
import example.Advanced.Music.app.dto.PlaylistDto;
import example.Advanced.Music.app.dto.SongDto;
import example.Advanced.Music.app.dto.UpdateSongRequest;
import example.Advanced.Music.app.entities.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

public interface PlaylistService {
    Page<ListPlayListDto> getAll(Pageable pageable) throws Exception;
    PlaylistDto findById(long id) throws Exception;
    List<ListPlayListDto> findByIds(List<Long> ids) throws Exception;
    String deleteById(long id) throws Exception;
    PlaylistDto createPlaylist(@Valid String playlistName) throws Exception;
    ListPlayListDto changeName(long id, String newName) throws Exception;
    String addSongToPlaylist(long playlistId, long songId) throws Exception;
    String deleteSongFromPlaylist(long playlistId, long songId) throws Exception;

}
