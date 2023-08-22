package example.Advanced.Music.app.services;

import example.Advanced.Music.app.dto.*;

import javax.validation.Valid;

public interface AuthService {
    String register(RegisterRequestDto requestDto) throws Exception;
    UserDto acceptRegister(String username, String opt) throws Exception;
    AuthSuccessResponseDto login(String username, String password) throws Exception;
    AuthSuccessResponseDto generateToken(String authorization) throws Exception;
    CurrentUserResponseDto currentUserInfo();
    String changePassword(@Valid ChangePasswordRequestDto requestDto) throws Exception;
    ForgetPasswordSuccessResponseDto forgetPassword(@Valid ForgetPasswordRequestDto requestDto) throws Exception;
    String resetPassword(String resetPasswordToken, @Valid ResetPasswordRequestDto request) throws Exception;
}
