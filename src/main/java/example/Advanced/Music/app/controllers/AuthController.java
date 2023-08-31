package example.Advanced.Music.app.controllers;

import example.Advanced.Music.app.Util.RequestUtil;
import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class AuthController {
    @Autowired
    private AuthService authService;
    @Value("${app.api.server.secret.key}")
    private String secretKey;

    @ApiOperation(value = "Api đăng ký tài khoản")
    @PostMapping("/register")
    @ResponseBody
    public SuccessResponse<Object> register(@RequestBody RegisterRequestDto requestDto) throws Exception {
        return RequestUtil.ok(authService.register(requestDto));
    }

    @ApiOperation(value = "Api accept tài khoản")
    @PostMapping("/accept-register")
    @ResponseBody
    public SuccessResponse<Object> acceptRegister(
            @RequestHeader(name = "username", required = true) @ApiParam(name = "username", value = "username", example = "testUser") String username,
            @RequestHeader(name = "otp", required = true) @ApiParam(name = "opt", value = "opt", example = "123123") String otp)
            throws Exception{
        return RequestUtil.ok(authService.acceptRegister(username,otp));
    }

    @ApiOperation(value = "Api đăng nhập")
    @GetMapping("/login")
    @ResponseBody
    public SuccessResponse<AuthSuccessResponseDto> login (
            @RequestHeader(name = "username", required = true) @ApiParam(name = "username", value = "username", example = "testUser") String username,
            @RequestHeader(name = "password", required = true) @ApiParam(name = "password", value = "password", example = "Qqwe@123") String password)
            throws Exception{
        return RequestUtil.ok(authService.login(username,password));
    }

    @ApiOperation(value = "API lấy token mới")
    @GetMapping("/generate-token")
    @ResponseBody
    public SuccessResponse<AuthSuccessResponseDto> generateToken(
            @RequestHeader(name = "refreshToken", required = false) @ApiParam(name = "refreshToken", value = "refresh", example = "refresh bbdgdfxbgfxgfdgdfgffh") String refreshToken)
            throws Exception{
            return RequestUtil.ok(authService.generateToken(refreshToken));
    }

    @ApiOperation(value = "API đổi mật khẩu")
    @PostMapping("/change-password")
    @ResponseBody
    public SuccessResponse<String> changePassword(@Valid @RequestBody ChangePasswordRequestDto request) throws Exception {
        return RequestUtil.ok(authService.changePassword(request));
    }

    @ApiOperation(value = "API Quên mật khẩu")
    @PostMapping("/forget-password")
    @ResponseBody
    public SuccessResponse<ForgetPasswordSuccessResponseDto> forgetPassword(@Valid @RequestBody ForgetPasswordRequestDto requestDto) throws Exception {
        return RequestUtil.ok(authService.forgetPassword(requestDto));
    }

    @ApiOperation(value = "API thiết lập lại mật khẩu")
    @PostMapping("/reset-password")
    @ResponseBody
    public SuccessResponse<String> resetPassword(
            @RequestHeader(name = "resetPasswordToken", required = true) @ApiParam(name = "resetPasswordToken", value = "reset password token", example = "resetPasswordToken") String resetPasswordToken,
            @Valid @RequestBody ResetPasswordRequestDto requestDto) throws Exception {
        return RequestUtil.ok(authService.resetPassword(resetPasswordToken,requestDto));
    }

    @ApiOperation(value = "API xem thông tin tài khoản hiện tại")
    @GetMapping("/me")
    @ResponseBody
    public SuccessResponse<CurrentUserResponseDto> currentUser() {return RequestUtil.ok(authService.currentUserInfo());}

}
