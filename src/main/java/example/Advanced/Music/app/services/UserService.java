package example.Advanced.Music.app.services;

import example.Advanced.Music.app.entities.Users;

import java.util.Optional;

public interface UserService {
    Optional<Users> findByUserName(String userName);
    boolean existsByUserName(String UserName);
    Users saveOrUpdate(Users user);
    Long count();

    String unlockUser(String username);
}
