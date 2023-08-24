package example.Advanced.Music.app.controllers;

import example.Advanced.Music.app.Util.RequestUtil;
import example.Advanced.Music.app.Util.SearchUtil;
import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.entities.User;
import example.Advanced.Music.app.enums.SortOrderEnum;
import example.Advanced.Music.app.models.CustomUserDetails;
import example.Advanced.Music.app.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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
        return new PageResponse<>(userDtoPage);
    }

    @ApiOperation(value = "API tìm tài khoản theo Id")
    @GetMapping("/{id}")
    @ResponseBody
    public SuccessResponse<UserDto> findById(@PathVariable long id) throws Exception {
        return RequestUtil.ok(userService.findById(id));
    }

}
