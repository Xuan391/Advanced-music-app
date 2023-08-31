package example.Advanced.Music.app.controllers;

import example.Advanced.Music.app.Util.RequestUtil;
import example.Advanced.Music.app.Util.SearchUtil;
import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.entities.*;
import example.Advanced.Music.app.enums.SortOrderEnum;
import example.Advanced.Music.app.models.CustomUserDetails;
import example.Advanced.Music.app.services.ImageStorageService;
import example.Advanced.Music.app.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    public UserService userService;

    @ApiOperation("API admin unlock tài khoản user")
    @PostMapping("/unlocked-account")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('" + Constants.Role.ROLE_ADMIN + "')")
    public SuccessResponse<Object> unlockAccount(
            @RequestParam (value = "username") String username ) throws Exception{
        return RequestUtil.ok(userService.unlockUser(username));
    }

    @ApiOperation("API admin tạo tài khoản")
    @PostMapping("/create-account")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('" + Constants.Role.ROLE_ADMIN + "')")
    public SuccessResponse<UserDto> createAccount(
            @Valid @RequestBody CreateUserRequest request) throws Exception{
        return RequestUtil.ok(userService.create(request));
    }

    @ApiOperation(value = "API cập nhật tài khoản quyền admin")
    @PostMapping("/update-by-admin/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('" + Constants.Role.ROLE_ADMIN + "')")
    public SuccessResponse<User> updateSomeFieldsByAdmin(@PathVariable("id") long id,
                                                  @Valid @RequestBody PatchRequest<UpdateUserRequest> request)
            throws Exception{
        return RequestUtil.ok(userService.update(id,request));
    }

    @ApiOperation(value = "API cập nhật tài ")
    @PostMapping("/update")
    @ResponseBody
    public SuccessResponse<User> updateSomeFields(@Valid @RequestBody PatchRequest<UpdateUserRequest> request)
            throws Exception{
        CustomUserDetails authUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return RequestUtil.ok(userService.update(authUser.getId(), request));
    }

    @ApiOperation(value = "API xóa tài khoản")
    @PostMapping("/delete/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('" + Constants.Role.ROLE_ADMIN + "')")
    public SuccessResponse<String> deleteUser(@PathVariable("id") Long id) throws Exception {
        return RequestUtil.ok(userService.deleteById(id));
    }

    @ApiOperation(value = "API xem tất cả các  tài khoản")
    @GetMapping("/get-all")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('"+Constants.Role.ROLE_ADMIN+"')")
    public PageResponse<UserDto> findAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0" )Integer page,
                                         @Positive @RequestParam(required = false, defaultValue = "20") Integer size,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(required = false) SortOrderEnum order) throws Exception{
        Pageable pageable =SearchUtil.getPageableFromParam(page, size, sort, order);
        Page<UserDto> userDtoPage = userService.findAll(pageable);
        return RequestUtil.page(userDtoPage);
    }

    @ApiOperation(value = "API tìm tài khoản theo Id")
    @GetMapping("/{id}")
    @ResponseBody
    public SuccessResponse<UserDto> findById(@PathVariable long id) throws Exception {
        return RequestUtil.ok(userService.findById(id));
    }

    @ApiOperation(value = "Tìm tài khoản theo danh sách id")
    @PostMapping("/search/search-by-ids")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('"+Constants.Role.ROLE_ADMIN+"')")
    public SuccessResponse<List<User>> findByIds(@RequestBody List<Long> ids)  throws Exception{
        return RequestUtil.ok(userService.findByIds(ids));
    }

    @ApiOperation(value = "Api tìm kiếm nâng cao")
    @PostMapping("/search")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('"+Constants.Role.ROLE_ADMIN+"')")
    public PageResponse<UserDto> advanceSearch(@Valid @RequestBody SearchUserRequest searchRequest,
                                               @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
                                               @Positive @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort,
                                               @RequestParam(required = false) SortOrderEnum order) throws Exception {
        Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
        Page<UserDto> pageData = userService.advanceSearch(searchRequest, pageable);
        return RequestUtil.page(pageData);
    }

    @ApiOperation(value = "Api thay đổi avatar User")
    @PostMapping("/change-avatar-by-admin/{userId}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('"+Constants.Role.ROLE_ADMIN+"')")
    public SuccessResponse<UserDto> changeAvatarByAdmin(@PathVariable long userId,
                                                 @RequestParam(name = "imageFile")MultipartFile file) throws Exception{
        return RequestUtil.ok(userService.changeAvatar(userId,file));
    }

    @ApiOperation(value = "Api thay đổi avatar User")
    @PostMapping("/change-avatar-by-user")
    @ResponseBody
    public SuccessResponse<UserDto> changeAvatar(@RequestParam(name = "imageFile")MultipartFile file) throws Exception{
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return RequestUtil.ok(userService.changeAvatar(userDetails.getId(),file));
    }

    @ApiOperation(value = "Api follow user")
    @PutMapping("/follow/{userId}")
    @ResponseBody
    public SuccessResponse<Object> followUser(@PathVariable long userId) throws Exception {
        return RequestUtil.ok(userService.followUser(userId));
    }

    @ApiOperation(value = "Api unfollow user")
    @PutMapping("/unfollow/{userId}")
    @ResponseBody
    public SuccessResponse<Object> unFollowUser(@PathVariable long userId) throws Exception {
        return RequestUtil.ok(userService.unfollowUser(userId));
    }

    @ApiOperation(value = "Api show danh sách followers")
    @GetMapping("/show-followers")
    @ResponseBody
    public PageResponse<UserDto> showFollowers(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
                                              @Positive @RequestParam(required = false) Integer size,
                                              @RequestParam(required = false) String sort,
                                              @RequestParam(required = false) SortOrderEnum order) throws Exception{
        Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
        Page<UserDto> pageData = userService.showFollowers(pageable);
        return RequestUtil.page(pageData);
    }

    @ApiOperation(value = "Api show lịch sử tìm kiếm")
    @GetMapping("/show-search-history")
    @ResponseBody
    public PageResponse<SearchHistory> showSearchHistory(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
                                               @Positive @RequestParam(required = false) Integer size,
                                               @RequestParam(required = false) String sort,
                                               @RequestParam(required = false) SortOrderEnum order) throws Exception{
        Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
        Page<SearchHistory> pageData = userService.showSearchHistoryOfUser(pageable);
        return RequestUtil.page(pageData);
    }

    @ApiOperation(value = "Api show lịch sử nghe nhạc")
    @GetMapping("/show-listened-history")
    @ResponseBody
    public PageResponse<ListenedHistory> showListenedHistory(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
                                                           @Positive @RequestParam(required = false) Integer size,
                                                           @RequestParam(required = false) String sort,
                                                           @RequestParam(required = false) SortOrderEnum order) throws Exception{
        Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
        Page<ListenedHistory> pageData = userService.showListenedHistoryOfUser(pageable);
        return RequestUtil.page(pageData);
    }

    @ApiOperation(value = "Api show playlist bằng userId")
    @GetMapping("/show-playlists/{userId}")
    @ResponseBody
    public PageResponse<Playlist> showPlaylist(@PathVariable long userId,
                                               @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
                                               @Positive @RequestParam(required = false) Integer size,
                                               @RequestParam(required = false) String sort,
                                               @RequestParam(required = false) SortOrderEnum order) throws Exception{
        Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
        Page<Playlist> pageData = userService.showPlaylistOfUser(userId,pageable);
        return RequestUtil.page(pageData);
    }

    @ApiOperation(value = "Api show song bằng userId")
    @GetMapping("/show-songs/{userId}")
    @ResponseBody
    public PageResponse<Song> showSongs(@PathVariable long userId,
                                           @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
                                           @Positive @RequestParam(required = false) Integer size,
                                           @RequestParam(required = false) String sort,
                                           @RequestParam(required = false) SortOrderEnum order) throws Exception{
        Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
        Page<Song> pageData = userService.showSongOfUser(userId,pageable);
        return RequestUtil.page(pageData);
    }


}
