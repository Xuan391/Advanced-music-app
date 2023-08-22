package example.Advanced.Music.app.controllers;

import example.Advanced.Music.app.Util.RequestUtil;
import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.dto.SuccessResponse;
import example.Advanced.Music.app.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


}
