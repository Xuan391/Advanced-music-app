package example.Advanced.Music.app.services;

import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String UserName);
    User saveOrUpdate(User user);
    Long count();

    String unlockUser(String username);

    UserDto create(@Valid CreateUserRequest request) throws Exception;

    User update(long id, @Valid PatchRequest<UpdateUserRequest> request) throws Exception;

    Page<UserDto> findAll(Pageable pageable) throws Exception;

    List<User> findByIds(List<Long> ids) throws Exception;

    String deleteById(long id) throws Exception;

    UserDto findById(long id) throws Exception;

    Page<UserDto> advanceSearch(@Valid SearchUserRequest searchRequest, Pageable pageable) throws Exception;
}
