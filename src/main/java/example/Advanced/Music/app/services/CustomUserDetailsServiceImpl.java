package example.Advanced.Music.app.services;

import example.Advanced.Music.app.entities.User;
import example.Advanced.Music.app.models.CustomUserDetails;
import example.Advanced.Music.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUsers = userRepository.findByUsername(username);
        if(optionalUsers.isPresent()){
            User user = optionalUsers.get();
            List<GrantedAuthority> authorityList = user.getListRoles().stream()
                    .map(roles -> new SimpleGrantedAuthority(roles.getRoleName().name()))
                    .collect(Collectors.toList());
            CustomUserDetails userDetail = new CustomUserDetails(user.getId(),user.getUsername(),
                     user.getPassword(),user.getDisplayName(), user.getIsLock());
            userDetail.setAuthorities(authorityList);
            return userDetail;
        }else {
            throw new UsernameNotFoundException(username + " not found");
        }

    }

//    private List<GrantedAuthority> getAuthorities(Users user) {
//        HashSet<String> lsRoleName = new HashSet<>();
//        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
//        while (userRoles.size() > 0) {
//            UserRole userRole = userRoles.get(0);
//            userRoles.remove(0);
//            lsRoleName.add(userRole.getRole().getName().getValue());
//        }
//        List<GrantedAuthority> authorities = lsRoleName.stream().map(role -> new SimpleGrantedAuthority(role))
//                .collect(Collectors.toList());
//        return authorities;
//    }



}
