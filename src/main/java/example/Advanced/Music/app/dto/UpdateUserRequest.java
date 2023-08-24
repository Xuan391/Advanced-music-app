package example.Advanced.Music.app.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
	private String username;
	private String firstName;
	private String lastName;
	private String email;
}
