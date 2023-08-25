package example.Advanced.Music.app.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUserRequest {
	private String username;
	private String name;
	private String email;
}
