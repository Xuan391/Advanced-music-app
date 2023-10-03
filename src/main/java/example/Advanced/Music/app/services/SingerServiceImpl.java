package example.Advanced.Music.app.services;

import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.entities.Playlist;
import example.Advanced.Music.app.entities.Singer;
import example.Advanced.Music.app.entities.SingerSong;
import example.Advanced.Music.app.entities.Song;
import example.Advanced.Music.app.enums.ErrorEnum;
import example.Advanced.Music.app.exception.ACTException;
import example.Advanced.Music.app.repositories.SingerRepository;
import example.Advanced.Music.app.repositories.SingerSongRepository;
import example.Advanced.Music.app.validator.Validator;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class SingerServiceImpl implements SingerService{
    @Autowired
    private SingerRepository singerRepository;
    @Autowired
    private SingerSongRepository singerSongRepository;

    @Override
    public Page<SingerDto> findAll(Pageable pageable) throws Exception {
        Page<Singer> page = singerRepository.findAll(pageable);
        List<SingerDto> list = new ArrayList<>();
        for(Singer singer : page){
            SingerDto singerDto = new SingerDto();
            singerDto.setId(singer.getId());
            singerDto.setName(singer.getName());
            int countSong = singerSongRepository.countBySinger(singer);
            singerDto.setSongCount(countSong);
            list.add(singerDto);
        }
        long totalElements = page.getTotalElements();
        return new PageImpl<>(list, page.getPageable(), totalElements);
    }

    @Override
    public SingerDetailDto findById(long id) throws Exception {
        Optional<Singer> optionalSinger = singerRepository.findById(id);
        if(!optionalSinger.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        Singer singer = optionalSinger.get();
        SingerDetailDto singerDetailDto = new SingerDetailDto();
        singerDetailDto.setSingerName(singer.getName());
        List<SingerSong> singerSongs = singerSongRepository.findBySinger(singer);
        List<SongDto> songDtos = new ArrayList<>();
        for (SingerSong singerSong : singerSongs){
            Song song = singerSong.getSong();
            SongDto songDto = new SongDto();
            PropertyUtils.copyProperties(songDto,song);
            songDto.setCreatorId(song.getCreator().getId());
            songDto.setNameCreator(song.getCreator().getUsername());
            songDtos.add(songDto);
        }
        singerDetailDto.setSongs(songDtos);
        return singerDetailDto;
    }

    @Override
    public List<SingerDto> findByIds(List<Long> ids) throws Exception {
        if(!Validator.isHaveDataLs(ids)){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        List<Singer> singers = singerRepository.findByIdIn(ids);
        List<SingerDto> singerDtos = new ArrayList<>();
        for(Singer singer: singers){
            SingerDto singerDto = new SingerDto();
            singerDto.setId(singer.getId());
            singerDto.setName(singer.getName());
            int countSong = singerSongRepository.countBySinger(singer);
            singerDto.setSongCount(countSong);
            singerDtos.add(singerDto);
        }
        return singerDtos;
    }

    @Override
    public String deleteById(long id) throws Exception {
        SingerDetailDto singerDetailDto = findById(id);
        Singer singerToDelete= singerRepository.findById(id)
                .orElseThrow(() -> new ACTException(ErrorEnum.DELETE_FAILUE,
                        "Delete song Failure: singer not found"));

        singerRepository.delete(singerToDelete);

        if (singerRepository.findById(id).isPresent()) {
            throw new ACTException(ErrorEnum.DELETE_FAILUE,
                    "Delete song Failure: singer " + singerDetailDto.getSingerName() + " is not deleted yet!");
        }
        return "Delete Success: song " + singerDetailDto.getSingerName() + " is deleted!";
    }

    @Override
    public SingerDto createSinger(String nameSinger) throws Exception {
        if(!singerRepository.existsByName(nameSinger)) {
            Singer singer = new Singer();
            singer.setName(nameSinger);
            Singer s = singerRepository.save(singer);
            singerRepository.flush();
            SingerDto singerDto = new SingerDto();
            singerDto.setId(s.getId());
            singerDto.setName(s.getName());
            singerDto.setSongCount(singerSongRepository.countBySinger(s));
            return singerDto;
        } else {
            throw new RuntimeException(" Singer name already exists ");
        }
    }

    @Override
    public SingerDto update(long id, String newName) throws Exception {
        Optional<Singer> optionalSinger = singerRepository.findById(id);
        if(!optionalSinger.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        Singer singer = optionalSinger.get();
        singer.setName(newName);
        Singer s = singerRepository.save(singer);
        SingerDto singerDto = new SingerDto();
        singerDto.setId(s.getId());
        singerDto.setName(s.getName());
        singerDto.setSongCount(singerSongRepository.countBySinger(s));
        return singerDto;
    }
}
