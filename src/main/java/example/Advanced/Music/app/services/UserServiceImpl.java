package example.Advanced.Music.app.services;

import example.Advanced.Music.app.entities.User;
import example.Advanced.Music.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUsername(userName);
    }

    @Override
    public User saveOrUpdate(User user) {
        return userRepository.save(user);
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

    @Override
    public String unlockUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Not found username:" + username));
        userRepository.unLockUser(user.getUsername());
        return user.getUsername()+": unlocked";
    }

}
