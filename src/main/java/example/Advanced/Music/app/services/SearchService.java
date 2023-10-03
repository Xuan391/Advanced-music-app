package example.Advanced.Music.app.services;

import example.Advanced.Music.app.dto.*;
import example.Advanced.Music.app.entities.*;
import example.Advanced.Music.app.enums.ErrorEnum;
import example.Advanced.Music.app.exception.ACTException;
import example.Advanced.Music.app.models.CustomUserDetails;
import example.Advanced.Music.app.repositories.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SearchService {
    @Autowired
    private SearchRepository searchRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private SingerRepository singerRepository;
    @Autowired
    private SingerSongRepository singerSongRepository;

    public List<SearchDto> getAll() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        User user = optionalUser.get();
        List<SearchHistory> searchHistories = searchRepository.findTop10ByUserOrderByCreatedDateDesc(user);
        List<SearchDto> list = new ArrayList<>();
        for(SearchHistory searchHistory : searchHistories){
            SearchDto searchDto = new SearchDto();
            searchDto.setSearchQuery(searchHistory.getSearchQuery());
            list.add(searchDto);
        }
        return list;
    }

    public SearchResultDto search(String searchQuery) throws Exception{
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if(!optionalUser.isPresent()){
            throw new ACTException(ErrorEnum.NOT_FOUND, ErrorEnum.NOT_FOUND.getMessageId());
        }
        User user = optionalUser.get();
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setSearchQuery(searchQuery);
        searchHistory.setUser(user);
        searchRepository.save(searchHistory);

        List<User> foundUsers = userRepository.searchUsersByName(searchQuery);
        List<Song> songs = songRepository.searchSongsByName(searchQuery);
        List<Playlist> playlists = playlistRepository.searchPlaylistByName(searchQuery);
        List<Singer> singers = singerRepository.searchSingerByByName(searchQuery);

        List<UserDto> userDtos = new ArrayList<>();
        for(User u : foundUsers){
            UserDto userDto = new UserDto();
            PropertyUtils.copyProperties(userDto, u);
            userDtos.add(userDto);
        }

        List<ListPlayListDto> playListDtoList = new ArrayList<>();
        for (Playlist playlist : playlists){
            ListPlayListDto listPlayListDto = new ListPlayListDto();
            PropertyUtils.copyProperties(listPlayListDto, playlist);
            listPlayListDto.setCreatorId(playlist.getCreator().getId());
            listPlayListDto.setCreatorName(playlist.getCreator().getUsername());
            playListDtoList.add(listPlayListDto);
        }

        List<SongDto> songDtos = new ArrayList<>();
        for(Song song: songs){
            SongDto songDto = new SongDto();
            PropertyUtils.copyProperties(songDto,song);
            songDto.setCreatorId(song.getCreator().getId());
            songDto.setNameCreator(song.getCreator().getUsername());
            songDtos.add(songDto);
        }

        List<SingerDto> singerDtos = new ArrayList<>();
        for(Singer singer : singers){
            SingerDto singerDto = new SingerDto();
            singerDto.setId(singer.getId());
            singerDto.setName(singer.getName());
            int countSong = singerSongRepository.countBySinger(singer);
            singerDto.setSongCount(countSong);
            singerDtos.add(singerDto);
        }

        SearchResultDto searchResultDto = new SearchResultDto();
        searchResultDto.setPlaylists(playListDtoList);
        searchResultDto.setSongs(songDtos);
        searchResultDto.setSingers(singerDtos);
        searchResultDto.setUsers(userDtos);

        return searchResultDto;
    }
}
