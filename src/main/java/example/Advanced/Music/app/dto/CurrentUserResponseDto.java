package example.Advanced.Music.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CurrentUserResponseDto {
    private Long id;
    private String username;
    private String displayName;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar_url;
    private List<String> roles;
}
