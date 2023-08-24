package example.Advanced.Music.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.entities.Playlist;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserDto {
	private long id;
	@JsonFormat(pattern = Constants.DEFAULT_DATE_FORMAT)
	private Date createDate;
	@JsonFormat(pattern = Constants.DEFAULT_DATE_FORMAT)
	private Date modifyDate;
	private String username;
	private String displayName;
	private String firstName;
	private String lastName;
	private String email;
	private String avatarUrl;
	private Boolean isLock;
	private List<String> roles;
}
