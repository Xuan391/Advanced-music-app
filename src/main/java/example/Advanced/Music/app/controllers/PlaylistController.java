package example.Advanced.Music.app.controllers;

import example.Advanced.Music.app.Util.RequestUtil;
import example.Advanced.Music.app.Util.SearchUtil;
import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.enums.SortOrderEnum;
import example.Advanced.Music.app.services.PlaylistService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/api/v1/playlist")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @ApiOperation(value = "API xem tất cả các playlist")
    @GetMapping("/get-all")
    @ResponseBody
    public SuccessResponse<Page<ListPlayListDto>> findAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
                                                 @Positive @RequestParam(required = false) Integer size,
                                                 @RequestParam(required = false) String sort,
                                                 @RequestParam(required = false) SortOrderEnum order) throws Exception {
        Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
        Page<ListPlayListDto> listPlayListDtoPage = playlistService.getAll(pageable);
        return RequestUtil.ok(listPlayListDtoPage);
    }

    @ApiOperation(value = "API tìm playlist theo id")
    @GetMapping("/{id}")
    @ResponseBody
    public SuccessResponse<PlaylistDto> findById(@PathVariable long id) throws Exception{
        return  RequestUtil.ok(playlistService.findById(id));
    }

    @ApiOperation(value = "API tìm các playlist theo danh sách id")
    @GetMapping("/search/search-by-ids")
    @ResponseBody
    public SuccessResponse<List<ListPlayListDto>> findByIds(@RequestBody List<Long> ids) throws Exception{
        return RequestUtil.ok(playlistService.findByIds(ids));
    }

    @ApiOperation(value = "API xóa playlist theo id")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public SuccessResponse<String> deletePlaylistById(@PathVariable long id) throws Exception{
        return RequestUtil.ok(playlistService.deleteById(id));
    }

    @ApiOperation(value = "Api tạo playlist")
    @PostMapping("/create")
    @ResponseBody
    public SuccessResponse<PlaylistDto> createPlaylist(@RequestParam ("name") String name ) throws Exception{
        return RequestUtil.ok(playlistService.createPlaylist(name));
    }

    @ApiOperation(value = "API thay đổi tên của playlist")
    @PutMapping("/change-name")
    @ResponseBody
    public SuccessResponse<ListPlayListDto> changeNamePlaylist(@RequestParam ("id") long id, @RequestParam ("newName") String newName) throws Exception{
        return RequestUtil.ok(playlistService.changeName(id, newName));
    }

    @ApiOperation(value = "API thêm bài hát vào playlist")
    @PostMapping("/add-song-to-playlist")
    @ResponseBody
    public SuccessResponse<String> addSongToPlaylist(@RequestParam ("playlistId") long playlistId, @RequestParam ("songId") long songId) throws Exception{
        return RequestUtil.ok(playlistService.addSongToPlaylist(playlistId, songId));
    }

    @ApiOperation(value = "API xóa bài hát khỏi playlist")
    @PutMapping("/remove-song-from-playlist")
    @ResponseBody
    public SuccessResponse<String> removeSongFromPlaylist(@RequestParam ("playlistId") long playlistId, @RequestParam ("songId") long songId) throws Exception{
        return RequestUtil.ok(playlistService.deleteSongFromPlaylist(playlistId, songId));
    }
}
