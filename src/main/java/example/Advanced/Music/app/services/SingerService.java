package example.Advanced.Music.app.services;

import example.Advanced.Music.app.dto.SingerDetailDto;
import example.Advanced.Music.app.dto.SingerDto;
import example.Advanced.Music.app.dto.SongDto;
import example.Advanced.Music.app.dto.UpdateSongRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

public interface SingerService {
    Page<SingerDto> findAll(Pageable pageable) throws Exception;
    SingerDetailDto findById(long id) throws Exception;
    List<SingerDto> findByIds(List<Long> ids) throws Exception;
    String deleteById(long id) throws Exception;
    SingerDto createSinger(@Valid String nameSinger) throws Exception;
    SingerDto update(long id, @Valid String newName) throws Exception;
}
