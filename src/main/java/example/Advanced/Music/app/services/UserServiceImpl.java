package example.Advanced.Music.app.services;

import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.entities.Role;
import example.Advanced.Music.app.entities.User;
import example.Advanced.Music.app.enums.ErrorEnum;
import example.Advanced.Music.app.enums.RoleEnum;
import example.Advanced.Music.app.exception.ACTException;
import example.Advanced.Music.app.repositories.RoleRepository;
import example.Advanced.Music.app.repositories.TokenRepository;
import example.Advanced.Music.app.repositories.UserRepository;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
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

    @Override
    public UserDto create(@Valid CreateUserRequest request) throws Exception {
        User u = new User();
        PropertyUtils.copyProperties(u, request);
        u.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Role> listRole = new HashSet<>();
        if(request.getRole() == null){
            request.setRole(RoleEnum.ROLE_USER);
        }
        Role role = roleRepository.findByRoleName(request.getRole()).orElseThrow(() -> new RuntimeException("can't found role name"));
        listRole.add(role);
        u.setListRoles(listRole);
        User user = userRepository.save(u);
        UserDto userDto = new UserDto();
        PropertyUtils.copyProperties(userDto, user);
        userDto.setCreateDate(Date.from(user.getCreatedDate()));
        userDto.setModifyDate(Date.from(user.getLastModifiedDate()));
        userDto.setRoles(Arrays.asList(role.getRoleName().name()));
        return userDto;
    }

    @Override
    public User update(long id,@Valid PatchRequest<UpdateUserRequest> request) throws Exception {
        Optional<User> oUser = userRepository.findById(id);
        if(!oUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        } else {
            try {
                User user = oUser.get();
                for(String fileName : request.getUpdateFields()){
                    Object newValue = PropertyUtils.getProperty(request.getData(), fileName);
                    PropertyUtils.setProperty(user, fileName, newValue);
                }
                User b = userRepository.save(user);
                return b;
            }catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public Page<UserDto> findAll(Pageable pageable) throws Exception {
        Page<User> page = userRepository.findAll(pageable);
        List<UserDto> list = new ArrayList<>();
        for(User user : page){
            UserDto userDto = new UserDto();
            PropertyUtils.copyProperties(userDto, user);
            list.add(userDto);
        }
        long totalElements = page.getTotalElements();
        return new PageImpl<>(list,page.getPageable(),totalElements);
    }

    @Override
    public String deleteById(long id) throws Exception {
        UserDto userDto = findById(id);
        tokenRepository.deleteByUserId(id);
        userRepository.deleteById(id);
        if (userRepository.findById(id).isPresent()) {
            throw new ACTException(ErrorEnum.DELETE_FAILUE,
                    "Delete User Failue: User " + userDto.getDisplayName() + " is not deleted yet!");
        }
        return "Delete Success: User " + userDto.getDisplayName() + " is deleted!";
    }

    @Override
    public UserDto findById(long id) throws Exception {
        Optional<User> oUser = userRepository.findById(id);
        if (!oUser.isPresent()) {
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        } else {
            UserDto userDto = new UserDto();
            PropertyUtils.copyProperties(userDto, oUser.get());
            Set<Role> roles = oUser.get().getListRoles();
            List<String> roleNames = new ArrayList<>();
            for (Role role : roles) {
                roleNames.add(role.getRoleName().name());
            }
            userDto.setRoles(roleNames);
            userDto.setCreateDate(Date.from(oUser.get().getCreatedDate()));
            userDto.setModifyDate(Date.from(oUser.get().getLastModifiedDate()));
            return userDto;
        }
    }

    @Override
    public List<User> findByIds(List<Long> ids) throws Exception {
        return null;
    }

    @Override
    public Page<UserDto> advanceSearch(SearchUserRequest searchRequest, Pageable pageable) throws Exception {
        return null;
    }

}
