package example.Advanced.Music.app.services;

import example.Advanced.Music.app.controllers.ImageFileController;
import example.Advanced.Music.app.controllers.SongFileController;
import example.Advanced.Music.app.dto.PatchRequest;
import example.Advanced.Music.app.dto.SongDto;
import example.Advanced.Music.app.dto.UpdateSongRequest;
import example.Advanced.Music.app.dto.UserDto;
import example.Advanced.Music.app.entities.Playlist;
import example.Advanced.Music.app.entities.Singer;
import example.Advanced.Music.app.entities.Song;
import example.Advanced.Music.app.entities.User;
import example.Advanced.Music.app.enums.ErrorEnum;
import example.Advanced.Music.app.exception.ACTException;
import example.Advanced.Music.app.models.CustomUserDetails;
import example.Advanced.Music.app.repositories.PlaylistRepository;
import example.Advanced.Music.app.repositories.SingerRepository;
import example.Advanced.Music.app.repositories.SongRepository;
import example.Advanced.Music.app.repositories.UserRepository;
import example.Advanced.Music.app.validator.Validator;
import nonapi.io.github.classgraph.utils.VersionFinder;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

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
    private PlaylistRepository playlistRepository;
    @Autowired
    private SingerRepository singerRepository;
    @Autowired
    private ImageStorageService imageStorageService;
    @Autowired
    private SongStorageService songStorageService;

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

//    @Override
//    public String deleteById(long id) throws Exception {
//        SongDto songDto = findById(id);
//        songRepository.deleteById(id);
//        if (songRepository.findById(id).isPresent()) {
//            throw new ACTException(ErrorEnum.DELETE_FAILUE,
//                    "Delete song Failue: song " + songDto.getName() + " is not deleted yet!");
//        }
//        return "Delete Success: song " + songDto.getName() + " is deleted!";
//    }
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
        if(imageFile == null){
            song.setThumbnailUrl(null);
        }else {
            String thumbnailFile = imageStorageService.storeFile(imageFile);
            String thumbnailUrl = MvcUriComponentsBuilder.fromMethodName(ImageFileController.class, "readDetailImageFile", thumbnailFile).build().toUri().toString();
            song.setThumbnailUrl(thumbnailUrl);
        }
        String songFileName = songStorageService.storeFile(songFile);
        String songUrl = MvcUriComponentsBuilder.fromMethodName(SongFileController.class,"readDetailSongFile", songFileName).build().toUri().toString();
        song.setSongUrl(songUrl);
        List<Singer> singers = new ArrayList<>();
        for(String nameSinger : nameSingers){
            Optional<Singer> optionalSinger = singerRepository.findByName(nameSinger.trim());
            if(!optionalSinger.isPresent()){
                Singer singer = new Singer();
                singer.setName(nameSinger);
                singerRepository.save(singer);
                singers.add(singer);
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
    public SongDto update(long id, PatchRequest<UpdateSongRequest> request) throws Exception {
        return null;
    }

    @Override
    public SongDto changeThumbnail(long id, MultipartFile imageFile) throws Exception {
        return null;
    }
}
