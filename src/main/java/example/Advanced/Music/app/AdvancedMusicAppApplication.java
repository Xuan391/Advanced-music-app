package example.Advanced.Music.app;

import example.Advanced.Music.app.entities.Roles;
import example.Advanced.Music.app.entities.User;
import example.Advanced.Music.app.enums.RoleEnum;
import example.Advanced.Music.app.services.RoleService;
import example.Advanced.Music.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication
public class AdvancedMusicAppApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AdvancedMusicAppApplication.class, args);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Value("${app.api.first-user}")
	private String username;
	@Value("${app.api.first-password}")
	private String pwd;
	@Value("${app.api.first-display-name}")
	private String displayName;
	@Value("${app.api.first-email}")
	private String email;
	@Value("${app.api.first-first-name}")
	private String firstName;
	@Value("${app.api.first-last-name}")
	private String lastName;

	@Override
	public void run(String... args) throws Exception {
		long count = userService.count();
		if(count == 0){
			try{
				List<Roles> roles = new ArrayList<>();
				for(RoleEnum r: RoleEnum.values()){
					Roles role = new Roles();
					role.setRoleName(r);
					roles.add(role);
				}
			}catch (Exception e){
				e.printStackTrace();
			}

			User user = new User();
			user.setUsername(username);
			user.setPassword(passwordEncoder.encode(pwd.trim()));
			user.setDisplayName(displayName);
			user.setEmail(email);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			Set<Roles> listRoles = new HashSet<>();
			List<Roles> roles = roleService.findAll();
			for(Roles role : roles){
				listRoles.add(role);
			}
			user.setListRoles(listRoles);
			userService.saveOrUpdate(user);
		}

	}

}
