package example.Advanced.Music.app.dto;

import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.validator.FieldMatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@FieldMatch(first = "newPassword", second = "confirmNewPassword")
public class ChangePasswordRequestDto {
    @NotNull
    @Pattern(regexp = Constants.PATTERN_PASSWORD)
    @ApiModelProperty(notes = "Current Password", example = "Xuan@123", required = true)
    private String currentPassword;
    @NotNull
    @Pattern(regexp = Constants.PATTERN_PASSWORD)
    @ApiModelProperty(notes = "New Password", example = "Thanhxuan@123", required = true)
    private String newPassword;
    @NotNull
    @Pattern(regexp = Constants.PATTERN_PASSWORD)
    @ApiModelProperty(notes = "Confirm New Password", example = "Thanhxuan@123", required = true)
    private String confirmNewPassword;

}
