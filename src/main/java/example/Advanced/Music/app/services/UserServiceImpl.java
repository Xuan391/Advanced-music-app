package example.Advanced.Music.app.services;

import example.Advanced.Music.app.Util.SearchUtil;
import example.Advanced.Music.app.controllers.ImageFileController;
import example.Advanced.Music.app.controllers.UserController;
import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.entities.*;
import example.Advanced.Music.app.enums.ErrorEnum;
import example.Advanced.Music.app.enums.RoleEnum;
import example.Advanced.Music.app.exception.ACTException;
import example.Advanced.Music.app.models.CustomUserDetails;
import example.Advanced.Music.app.repositories.*;
import example.Advanced.Music.app.validator.Validator;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

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
    private SongRepository songRepository;
    @Autowired
    private UserDownloadSongRepository userDownloadSongRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ImageStorageService imageStorageService;
    @Autowired
    private ListenedHistoryRepository listenedHistoryRepository;
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
        if (!Validator.isHaveDataLs(ids)){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        List<User> users = userRepository.findByIdIn(ids);
        if(!Validator.isHaveDataLs(users)){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        } else {
            return users;
        }
    }

    @Override
    public Page<UserDto> advanceSearch(@Valid SearchUserRequest searchRequest, Pageable pageable) throws Exception {
        if (searchRequest != null) {
            List<Specification<User>> specList = getAdvanceSearchSpecList(searchRequest);
            if(specList.size()>0){
                Specification<User> spec = specList.get(0);
                for (int i = 1; i< specList.size(); i++){
                    spec = spec.and(specList.get(i));
                }
                Page<User> page = userRepository.findAll(spec, pageable);
                List<UserDto> userDtos = new ArrayList<>();
                for(User u: page){
                    UserDto userDto = new UserDto();
                    PropertyUtils.copyProperties(userDto, u);
                    userDtos.add(userDto);
                }
                long totalElement = page.getTotalElements();
                return new PageImpl<>(userDtos, page.getPageable(), totalElement);
            }
        }
        return null;
    }

    private List<Specification<User>> getAdvanceSearchSpecList(@Valid SearchUserRequest sRequest){
        List<Specification<User>> specList = new ArrayList<>();
        if(Validator.isHaveDataString(sRequest.getUsername())){
            specList.add(SearchUtil.like("username", "%" + sRequest.getUsername()+"%"));
        }
        if(Validator.isHaveDataString(sRequest.getName())){
            specList.add(SearchUtil.like("displayName", "%"+ sRequest.getName()+"%"));
        }
        if (Validator.isHaveDataString(sRequest.getEmail())) {
            specList.add(SearchUtil.like("email", "%" + sRequest.getEmail() + "%"));
        }
        return specList;
    }

    @Override
    public UserDto changeAvatar(long userId, MultipartFile file) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        User user = optionalUser.get();
        if(file != null && !file.isEmpty()){
            String imageFile = imageStorageService.storeFile(file);
            String avatarUrl = "api/Songs/imageFiles"+ imageFile;
            user.setAvatarUrl(avatarUrl);
        } else {
            user.setAvatarUrl(null);
        }
        User u = userRepository.save(user);
        UserDto userDto = new UserDto();
        PropertyUtils.copyProperties(userDto, u);
        return userDto;
    }

    @Override
    public String followUser(long followUserId) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        Optional<User> optionalFollowUser = userRepository.findById(followUserId);
        if (!optionalFollowUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        try {
            User user = optionalUser.get();
            User followUser = optionalFollowUser.get();
            Set<User> followers = followUser.getFollowers();
            followers.add(user);
            followUser.setFollowers(followers);
            followUser.setFollowersCount(followers.size());
            userRepository.save(followUser);
            return "Already follow: "+ followUser.getDisplayName();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String unfollowUser(long userUnfollowUserId) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        Optional<User> optionalUnfollowUser = userRepository.findById(userUnfollowUserId);
        if (!optionalUnfollowUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        try {
            User user = optionalUser.get();
            User unfollowUser = optionalUnfollowUser.get();
            unfollowUser.getFollowers().remove(user);
            unfollowUser.setFollowersCount(unfollowUser.getFollowers().size());
            userRepository.save(unfollowUser);
            return "you unfollowed" + unfollowUser.getDisplayName();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Page<UserDto> showFollowers(Pageable pageable) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if (!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        User user = optionalUser.get();
        Set<User> followers =  user.getFollowers();
        List<UserDto> userDtos = new ArrayList<>();
        for (User u : followers){
            UserDto userDto = new UserDto();
            PropertyUtils.copyProperties(userDto, u);
            userDtos.add(userDto);
        }
        long totalElements = followers.size();
        return new PageImpl<>(userDtos, pageable, totalElements);
    }

    @Override
    public Page<Playlist> showPlaylistOfUser(long userId, Pageable pageable) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        User user = optionalUser.get();
        List<Playlist> playlists = user.getPlaylists();
        long totalElements = playlists.size();
        return new PageImpl<>(playlists, pageable, totalElements);
    }

    @Override
    public Page<Song> showSongOfUser(long userId, Pageable pageable) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        User user = optionalUser.get();
        List<Song> songs = (List<Song>) user.getSong();
        long totalElements = songs.size();
        return new PageImpl<>(songs, pageable, totalElements);
    }

    @Override
    public Page<SearchHistory> showSearchHistoryOfUser(Pageable pageable) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        User user = optionalUser.get();
        List<SearchHistory> searchHistories = user.getSearchHistories();
        long totalElements = searchHistories.size();
        return new PageImpl<>(searchHistories, pageable, totalElements);
    }



    @Override
    public Page<SongDto> showListenedHistoryOfUser(Pageable pageable) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        User user = optionalUser.get();
        List<Song> songs = listenedHistoryRepository.findByUserOrderByCreatedDateDesc(user);
        List<SongDto> songDtos = new ArrayList<>();
        for(Song song: songs){
            SongDto songDto = new SongDto();
            PropertyUtils.copyProperties(songDto,song);
            songDto.setCreatorId(song.getCreator().getId());
            songDto.setNameCreator(song.getCreator().getUsername());
            songDtos.add(songDto);
        }
        long totalElements = songDtos.size();
        return new PageImpl<>(songDtos, pageable, totalElements);
    }

    @Override
    public String downloadSong (long songId) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        Optional<Song> optionalSong = songRepository.findById(songId);
        if(!optionalSong.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }

        User user = optionalUser.get();
        Song song = optionalSong.get();

        UserDownloadSong userDownloadSong = new UserDownloadSong();
        userDownloadSong.setUserId(user.getId());
        userDownloadSong.setSongId(song.getId());
        userDownloadSongRepository.save(userDownloadSong);
        return "Download song: "+song.getName()+" successfully";
    }

    @Override
    public List<SongDto> getDownloadSongs() throws Exception{
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        User user = optionalUser.get();
        List<UserDownloadSong> userDownloadSongs = userDownloadSongRepository.findByUserId(user.getId());
        List<SongDto> songDtos = new ArrayList<>();
        for(UserDownloadSong userDownloadSong: userDownloadSongs){
            Song song = userDownloadSong.getSong();
            SongDto songDto = new SongDto();
            PropertyUtils.copyProperties(songDto, song);
            songDto.setCreatorId(song.getCreator().getId());
            songDto.setNameCreator(song.getCreator().getUsername());
            songDtos.add(songDto);
        }
        return songDtos;
    }

}
