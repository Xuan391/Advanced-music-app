package example.Advanced.Music.app.services;

import example.Advanced.Music.app.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String UserName);
    User saveOrUpdate(User user);
    Long count();

    String unlockUser(String username);


}
