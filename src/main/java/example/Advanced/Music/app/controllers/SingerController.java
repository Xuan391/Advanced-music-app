package example.Advanced.Music.app.controllers;

import example.Advanced.Music.app.Util.RequestUtil;
import example.Advanced.Music.app.Util.SearchUtil;
import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.enums.SortOrderEnum;
import example.Advanced.Music.app.services.SingerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/api/v1/singer")
public class SingerController {
    @Autowired
    private SingerService singerService;

    @ApiOperation(value = "API xem tất cả các Singer")
    @GetMapping("/get-all")
    @ResponseBody
    public SuccessResponse<Page<SingerDto>> findAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
                                                          @Positive @RequestParam(required = false) Integer size,
                                                          @RequestParam(required = false) String sort,
                                                          @RequestParam(required = false) SortOrderEnum order) throws Exception {
        Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
        Page<SingerDto> singerDtos = singerService.findAll(pageable);
        return RequestUtil.ok(singerDtos);
    }

    @ApiOperation(value = "API lấy ra các bài hát của singer theo SingerId")
    @GetMapping("/{id}")
    @ResponseBody
    public SuccessResponse<SingerDetailDto> findById(@PathVariable long id) throws Exception{
        return  RequestUtil.ok(singerService.findById(id));
    }

    @ApiOperation(value = "API tìm các singer theo danh sách id")
    @GetMapping("/search/search-by-ids")
    @ResponseBody
    public SuccessResponse<List<SingerDto>> findByIds(@RequestBody List<Long> ids) throws Exception{
        return RequestUtil.ok(singerService.findByIds(ids));
    }

    @ApiOperation(value = "API xóa singer theo id")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public SuccessResponse<String> deletePlaylistById(@PathVariable long id) throws Exception{
        return RequestUtil.ok(singerService.deleteById(id));
    }

    @ApiOperation(value = "Api tạo singer")
    @PostMapping("/create")
    @ResponseBody
    public SuccessResponse<SingerDto> createPlaylist(@RequestParam ("name") String name ) throws Exception{
        return RequestUtil.ok(singerService.createSinger(name));
    }

    @ApiOperation(value = "API thay đổi tên của singer")
    @PutMapping("/change-name")
    @ResponseBody
    public SuccessResponse<SingerDto> changeNamePlaylist(@RequestParam ("id") long id, @RequestParam ("newName") String newName) throws Exception{
        return RequestUtil.ok(singerService.update(id, newName));
    }
}
