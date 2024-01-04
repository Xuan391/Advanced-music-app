package example.Advanced.Music.app.services;

import example.Advanced.Music.app.dto.SongDto;
import example.Advanced.Music.app.dto.UpdateSongRequest;
import example.Advanced.Music.app.entities.*;
import example.Advanced.Music.app.enums.ErrorEnum;
import example.Advanced.Music.app.exception.ACTException;
import example.Advanced.Music.app.models.CustomUserDetails;
import example.Advanced.Music.app.repositories.*;
import example.Advanced.Music.app.validator.Validator;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SongServiceImpl implements SongService{
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SingerRepository singerRepository;
    @Autowired
    private ImageStorageService imageStorageService;
    @Autowired
    private SongStorageService songStorageService;
    @Autowired
    private ListenedHistoryRepository listenedHistoryRepository;

    @Override
    public Page<SongDto> findAll(Pageable pageable) throws Exception {
        Page<Song> page = songRepository.findAll(pageable);
        List<SongDto> list = new ArrayList<>();
        for(Song song: page){
            SongDto songDto = new SongDto();
            PropertyUtils.copyProperties(songDto,song);
            songDto.setCreatorId(song.getCreator().getId());
            songDto.setNameCreator(song.getCreator().getUsername());
            list.add(songDto);
        }
        long totalElements = page.getTotalElements();
        return new PageImpl<>(list, page.getPageable(), totalElements);
    }

    @Override
    public SongDto findById(long id) throws Exception {
        Optional<Song> optionalSong = songRepository.findById(id);
        if(!optionalSong.isPresent()){
            throw  new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        Song song = optionalSong.get();
        SongDto songDto = new SongDto();
        PropertyUtils.copyProperties(songDto,song);
        songDto.setCreatorId(song.getCreator().getId());
        songDto.setNameCreator(song.getCreator().getUsername());
        return songDto;
    }

    @Override
    public List<SongDto> findByIds(List<Long> ids) throws Exception {
        if(!Validator.isHaveDataLs(ids)){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        List<Song> songs = songRepository.findByIdIn(ids);
        List<SongDto> songDtos = new ArrayList<>();
        for (Song song: songs){
            SongDto songDto = new SongDto();
            PropertyUtils.copyProperties(songDto, song);
            songDto.setCreatorId(song.getCreator().getId());
            songDto.setNameCreator(song.getCreator().getUsername());
            songDtos.add(songDto);
        }
        if(!Validator.isHaveDataLs(songDtos)){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        } else {
            return songDtos;
        }
    }

    @Override
    public String deleteById(long id) throws Exception {
        SongDto songDto = findById(id);

        Song songToDelete = songRepository.findById(id)
                .orElseThrow(() -> new ACTException(ErrorEnum.DELETE_FAILUE,
                        "Delete song Failure: Song not found"));

        // Xóa các liên kết liên quan đến bài hát trong các bảng khác
        songToDelete.getSingers().clear();  // Xóa liên kết với singers
        songToDelete.getListenedHistories().clear();  // Xóa liên kết với listenedHistories
        songToDelete.getPlaylistSongs().clear();  // Xóa liên kết với playlistSongs

        songRepository.delete(songToDelete);

        if (songRepository.findById(id).isPresent()) {
            throw new ACTException(ErrorEnum.DELETE_FAILUE,
                    "Delete song Failure: song " + songDto.getName() + " is not deleted yet!");
        }
        return "Delete Success: song " + songDto.getName() + " is deleted!";
    }

    @Override
    public SongDto createSong(String nameSong, MultipartFile imageFile, MultipartFile songFile, List<String> nameSingers) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        User user = optionalUser.get();
        Song song = new Song();
        song.setCreator(user);
        song.setName(nameSong);
        if(imageFile == null || imageFile.isEmpty()){
            song.setThumbnailUrl(null);
        }else {
            String thumbnailFile = imageStorageService.storeFile(imageFile);
            String thumbnailUrl = "/api/v1/imageFiles/" + thumbnailFile;
            song.setThumbnailUrl(thumbnailUrl);
        }
        String songFileName = songStorageService.storeFile(songFile);
        String songUrl = "/api/v1/music/musicFiles/" + songFileName;
        song.setSongUrl(songUrl);
        List<Singer> singers = new ArrayList<>();
        for(String nameSinger : nameSingers){
            Optional<Singer> optionalSinger = singerRepository.findByName(nameSinger.trim());
            if(!optionalSinger.isPresent()){
                Singer singer = new Singer();
                singer.setName(nameSinger);
                Singer s = singerRepository.save(singer);
                singers.add(s);
            } else {
                Singer singer = optionalSinger.get();
                singers.add(singer);
            }
        }
        song.setSingers(singers);
        songRepository.save(song);
        SongDto songDto = new SongDto();
        PropertyUtils.copyProperties(songDto,song);
        songDto.setCreatorId(song.getCreator().getId());
        songDto.setNameCreator(song.getCreator().getUsername());
        return songDto;
    }

    @Override
    public SongDto update(long id, UpdateSongRequest request) throws Exception {
        Optional<Song> optionalSong = songRepository.findById(id);
        if(!optionalSong.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        } else {
            try {
                Song song = optionalSong.get();
                if (request.getName() != null){
                    song.setName(request.getName());
                }
                if (request.getSingers() != null && request.getSingers().size() > 0){
                    List<Singer> singers = new ArrayList<>();
                    for (String singerName : request.getSingers()){
                        if(!singerRepository.existsByName(singerName)){
                            Singer singer = new Singer();
                            singer.setName(singerName);
                            singerRepository.save(singer);
                            singerRepository.flush();
                            singers.add(singer);
                        } else {
                            Optional<Singer> optionalSinger = singerRepository.findByName(singerName);
                            Singer singer = optionalSinger.get();
                            singers.add(singer);
                        }
                    }
                    song.setSingers(singers);
                }
                Song b = songRepository.save(song);
                SongDto songDto = new SongDto();
                PropertyUtils.copyProperties(songDto,song);
                songDto.setCreatorId(song.getCreator().getId());
                songDto.setNameCreator(song.getCreator().getUsername());
                return songDto;
            }catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public SongDto changeThumbnail(long id, MultipartFile imageFile) throws Exception {
        Optional<Song> optionalSong = songRepository.findById(id);
        if(!optionalSong.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        } else {
            Song song = optionalSong.get();
            if(imageFile != null && !imageFile.isEmpty()){
                String thumbnailFile = imageStorageService.storeFile(imageFile);
                String thumbnailUrl = "/api/v1/imageFiles/" + thumbnailFile;
                song.setThumbnailUrl(thumbnailUrl);
            } else {
                song.setThumbnailUrl(null);
            }
            Song s = songRepository.save(song);
            SongDto songDto = new SongDto();
            PropertyUtils.copyProperties(songDto, s);
            songDto.setCreatorId(s.getCreator().getId());
            songDto.setNameCreator(s.getCreator().getUsername());
            return songDto;
        }
    }

    @Override
    public SongDto changeLyric(long id, String lyric) throws Exception{
        Optional<Song> optionalSong = songRepository.findById(id);
        if(!optionalSong.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        } else {
            Song song = optionalSong.get();
            song.setLyric(lyric);
            Song s = songRepository.save(song);
            SongDto songDto = new SongDto();
            PropertyUtils.copyProperties(songDto, s);
            songDto.setCreatorId(s.getCreator().getId());
            songDto.setNameCreator(s.getCreator().getUsername());
            return songDto;
        }
    }

    @Override
    public SongDto listenedSong(long songId) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if (!optionalUser.isPresent()) {
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        Optional<Song> optionalSong = songRepository.findById(songId);
        if (!optionalSong.isPresent()) {
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        User user = optionalUser.get();
        Song song = optionalSong.get();
        ListenedHistory listenedHistory = new ListenedHistory(user, song);
        listenedHistoryRepository.save(listenedHistory);

        song.setListenedCount(song.getListenedCount() + 1);
        Song s = songRepository.save(song);
        SongDto songDto = new SongDto();
        PropertyUtils.copyProperties(songDto, s);
        songDto.setCreatorId(s.getCreator().getId());
        songDto.setNameCreator(s.getCreator().getUsername());
        return songDto;

    }
}
