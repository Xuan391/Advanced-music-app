package example.Advanced.Music.app.controllers;

import example.Advanced.Music.app.Util.RequestUtil;
import example.Advanced.Music.app.Util.SearchUtil;
import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.enums.SortOrderEnum;
import example.Advanced.Music.app.services.ImageStorageService;
import example.Advanced.Music.app.services.SongService;
import example.Advanced.Music.app.services.SongStorageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/api/v1/song")
public class SongController {
    @Autowired
    private SongService songService;

    @ApiOperation(value = "API xem tất cả các bài hát")
    @GetMapping("/get-all")
    @ResponseBody
    public PageResponse<SongDto> findAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
                                         @Positive @RequestParam(required = false) Integer size,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(required = false) SortOrderEnum order) throws Exception {
        Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
        Page<SongDto> songDtoPage = songService.findAll(pageable);
        return RequestUtil.page(songDtoPage);
    }

    @ApiOperation(value = "API tìm bài hát theo id")
    @GetMapping("/{songId}")
    @ResponseBody
    public SuccessResponse<SongDto> findById(@PathVariable long songId) throws Exception{
        return  RequestUtil.ok(songService.findById(songId));
    }

    @ApiOperation(value = "API tìm các bài hát theo danh sách id")
    @GetMapping("/search/search-by-ids")
    @ResponseBody
    public SuccessResponse<List<SongDto>> findByIds(@RequestBody List<Long> ids) throws Exception{
        return RequestUtil.ok(songService.findByIds(ids));
    }

    @ApiOperation(value = "API xóa bài hát theo id")
    @DeleteMapping("/delete/{songId}")
    @ResponseBody
    public SuccessResponse<String> deleteSongById(@PathVariable long songId) throws Exception{
        return RequestUtil.ok(songService.deleteById(songId));
    }

    @ApiOperation(value = "API tạo bài hát mới")
    @PostMapping("/create")
    @ResponseBody
    public SuccessResponse<SongDto> createSong(@RequestParam("name") String nameSong, @RequestParam("image") MultipartFile imageFile,
                                               @RequestParam("song") MultipartFile songFile,@RequestParam("singers") List<String> nameSingers)
            throws Exception {
        return RequestUtil.ok(songService.createSong(nameSong, imageFile, songFile, nameSingers));
    }

    @ApiOperation(value = "Api cập nhật tên bài hát và ca sĩ hát bài hát đó")
    @PutMapping("/update/{id}")
    @ResponseBody
    public SuccessResponse<SongDto> updateSong(@PathVariable long id, @Valid @RequestBody UpdateSongRequest request) throws Exception {
        return RequestUtil.ok(songService.update(id,request));
    }

    @ApiOperation(value = "Api thay đổi thumbnail bài hát")
    @PutMapping("/change-thumbnail/{id}")
    @ResponseBody
    public SuccessResponse<SongDto> changeThumbnail(@PathVariable long id, @RequestParam("imageFile") MultipartFile imageFile) throws Exception{
        return RequestUtil.ok(songService.changeThumbnail(id, imageFile));
    }

    @ApiOperation(value = "Api cập nhật lyric bài hát")
    @PutMapping("/change-lyric/{id}")
    @ResponseBody
    public SuccessResponse<SongDto> changeLyric(@PathVariable long id, @RequestParam("lyric") String lyric) throws Exception{
        return RequestUtil.ok(songService.changeLyric(id, lyric));
    }

    @ApiOperation(value = "Api tăng lươt nghe bài hát khi user nghe nhạc")
    @PutMapping("/listened-count/{id}")
    @ResponseBody
    public SuccessResponse<SongDto> listenedCount(@PathVariable long id) throws Exception{
        return RequestUtil.ok(songService.listenedSong(id));
    }

}
