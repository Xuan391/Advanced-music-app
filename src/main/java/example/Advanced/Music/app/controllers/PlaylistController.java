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
    public PageResponse<ListPlayListDto> findAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
                                                 @Positive @RequestParam(required = false) Integer size,
                                                 @RequestParam(required = false) String sort,
                                                 @RequestParam(required = false) SortOrderEnum order) throws Exception {
        Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
        Page<ListPlayListDto> listPlayListDtoPage = playlistService.getAll(pageable);
        return RequestUtil.page(listPlayListDtoPage);
    }

    @ApiOperation(value = "API tìm playlist theo id")
    @GetMapping("/{id}")
    @ResponseBody
    public SuccessResponse<PlaylistDto> findById(@PathVariable long id) throws Exception{
        return  RequestUtil.ok(playlistService.findById(id));
    }

//    @ApiOperation(value = "API tìm các bài hát theo danh sách id")
//    @GetMapping("/search/search-by-ids")
//    @ResponseBody
//    public SuccessResponse<List<SongDto>> findByIds(@RequestBody List<Long> ids) throws Exception{
//        return RequestUtil.ok(songService.findByIds(ids));
//    }
}
