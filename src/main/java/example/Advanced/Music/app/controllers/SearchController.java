package example.Advanced.Music.app.controllers;

import com.act.test.security.validator.FieldExists;
import example.Advanced.Music.app.Util.RequestUtil;
import example.Advanced.Music.app.dto.SearchDto;
import example.Advanced.Music.app.dto.SearchResultDto;
import example.Advanced.Music.app.dto.SuccessResponse;
import example.Advanced.Music.app.services.SearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/searchHistory")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @ApiOperation(value = "API lấy ra 10 kết quả search gần đây")
    @GetMapping("")
    @ResponseBody
    public SuccessResponse<List<SearchDto>> getAll(){
        return RequestUtil.ok(searchService.getAll());
    }

    @ApiOperation(value = "API tìm kiếm theo keyword")
    @GetMapping("/search")
    @ResponseBody
    public SuccessResponse<SearchResultDto> search(@RequestParam("searchText") String searchText) throws Exception {
        return RequestUtil.ok(searchService.search(searchText));
    }

}
