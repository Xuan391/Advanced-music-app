package example.Advanced.Music.app.services;

import example.Advanced.Music.app.dto.ListPlayListDto;
import example.Advanced.Music.app.dto.PlaylistDto;
import example.Advanced.Music.app.dto.SongDto;
import example.Advanced.Music.app.entities.Playlist;
import example.Advanced.Music.app.entities.Song;
import example.Advanced.Music.app.entities.User;
import example.Advanced.Music.app.enums.ErrorEnum;
import example.Advanced.Music.app.exception.ACTException;
import example.Advanced.Music.app.models.CustomUserDetails;
import example.Advanced.Music.app.repositories.PlaylistRepository;
import example.Advanced.Music.app.repositories.SongRepository;
import example.Advanced.Music.app.repositories.UserRepository;
import example.Advanced.Music.app.validator.Validator;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<ListPlayListDto> getAll(Pageable pageable) throws Exception {
        Page<Playlist> page = playlistRepository.findAll(pageable);
        List<ListPlayListDto> list = new ArrayList<>();
        for (Playlist playlist : page) {
            ListPlayListDto listPlayListDto = new ListPlayListDto();
            PropertyUtils.copyProperties(listPlayListDto, playlist);
            listPlayListDto.setCreatorId(playlist.getCreator().getId());
            listPlayListDto.setCreatorName(playlist.getCreator().getUsername());
            list.add(listPlayListDto);
        }
        long totalElements = page.getTotalElements();
        return new PageImpl<>(list, page.getPageable(), totalElements);
    }
//    Page<Playlist> page = playlistRepository.findAll(pageable);
//    List<PlaylistDto> list = new ArrayList<>();
//        for (Playlist playlist : page) {
//        PlaylistDto playlistDto = new PlaylistDto();
//        PropertyUtils.copyProperties(playlistDto, playlist);
//        playlistDto.setCreatorId(playlist.getCreator().getId());
//        List<SongDto> songDtos = new ArrayList<>();
//        for (Song song : playlist.getSongs()) {
//            SongDto songDto = new SongDto();
//            PropertyUtils.copyProperties(songDto, song);
//            songDto.setCreatorId(song.getCreator().getId());
//            songDto.setNameCreator(song.getCreator().getUsername());
//            songDtos.add(songDto);
//        }
//        playlistDto.setSongDtoList(songDtos);
//        list.add(playlistDto);
//    }
//    long totalElements = page.getTotalElements();
//        return new PageImpl<>(list, page.getPageable(), totalElements);

    @Override
    public PlaylistDto findById(long id) throws Exception {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        if(!optionalPlaylist.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        Playlist playlist = optionalPlaylist.get();
        PlaylistDto playlistDto = new PlaylistDto();
        PropertyUtils.copyProperties(playlistDto, playlist);
        playlistDto.setCreatorId(playlist.getCreator().getId());
        List<SongDto> list = new ArrayList<>();
        for(Song song : playlist.getSongs()){
            SongDto songDto = new SongDto();
            PropertyUtils.copyProperties(songDto, song);
            songDto.setCreatorId(song.getCreator().getId());
            songDto.setNameCreator(song.getCreator().getUsername());
            list.add(songDto);
        }
        playlistDto.setSongDtoList(list);
        return playlistDto;
    }

    @Override
    public List<ListPlayListDto> findByIds(List<Long> ids) throws Exception {
        if(!Validator.isHaveDataLs(ids)){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        List<Playlist> playlists = playlistRepository.findByIdIn(ids);
        List<ListPlayListDto> listPlayListDtos = new ArrayList<>();
        for (Playlist playlist : playlists){
            ListPlayListDto listPlayListDto = new ListPlayListDto();
            PropertyUtils.copyProperties(listPlayListDto, playlist);
            listPlayListDto.setCreatorId(playlist.getCreator().getId());
            listPlayListDto.setCreatorName(playlist.getCreator().getUsername());
            listPlayListDtos.add(listPlayListDto);
        }
        if (!Validator.isHaveDataLs(listPlayListDtos)){
            throw  new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        return listPlayListDtos;
    }

    @Override
    public String deleteById(long id) throws Exception {
        PlaylistDto playlistDto = findById(id);
        if(!playlistDto.isFavorite()) {
            Playlist playlistToDelete = playlistRepository.findById(id).orElseThrow(() -> new ACTException(ErrorEnum.DELETE_FAILUE, "Delete playlist failure: Playlist not found"));
            try {
                playlistToDelete.getSongs().clear();
                playlistRepository.delete(playlistToDelete);
            } catch (Exception e){
                throw new Exception("delete false");
            }
            if (playlistRepository.findById(id).isPresent()) {
                throw new ACTException(ErrorEnum.DELETE_FAILUE, "Delete playlist failfure: playlist " + playlistDto.getName() + " is not delete yet");
            }
            return "Delete success: playlist " + playlistDto.getName() + " is deleted";
        } else {
            throw new ACTException(ErrorEnum.DELETE_FAILUE, "You can not delete this playlist because It is favorite playlist of user  ");
        }
    }

    @Override
    public PlaylistDto createPlaylist(String playlistName) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }

        User user = optionalUser.get();
        Playlist playlist = new Playlist();
        playlist.setName(playlistName);
        playlist.setCreator(user);
        playlist.setFavorite(false);
        Playlist p = playlistRepository.save(playlist);
        PlaylistDto playlistDto = new PlaylistDto();
        PropertyUtils.copyProperties(playlistDto, p);
        playlistDto.setCreatorId(p.getCreator().getId());

        return playlistDto;
    }

    @Override
    public ListPlayListDto changeName(long id, String newName) throws Exception {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        if(!optionalPlaylist.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        Playlist playlist = optionalPlaylist.get();
        playlist.setName(newName);
        Playlist p = playlistRepository.save(playlist);
        ListPlayListDto listPlayListDto = new ListPlayListDto();
        PropertyUtils.copyProperties(listPlayListDto, p);
        listPlayListDto.setCreatorId(p.getCreator().getId());
        return listPlayListDto;
    }

    @Override
    public String addSongToPlaylist(long playlistId, long songId) throws Exception {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
        Optional<Song> optionalSong = songRepository.findById(songId);
        if(!optionalPlaylist.isPresent() || !optionalSong.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        } else {
            Playlist playlist = optionalPlaylist.get();
            Song song = optionalSong.get();
            playlist.getSongs().add(song);
            playlistRepository.save(playlist);
            return "Added song to playlist successfully";
        }
    }

    @Override
    public String deleteSongFromPlaylist(long playlistId, long songId) throws Exception {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
        Optional<Song> optionalSong = songRepository.findById(songId);
        if(!optionalPlaylist.isPresent() || !optionalSong.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        } else {
            Playlist playlist = optionalPlaylist.get();
            Song song = optionalSong.get();
            playlist.getSongs().remove(song);
            playlistRepository.save(playlist);
            return "Successfully deleted song from playlist";
        }
    }
}
