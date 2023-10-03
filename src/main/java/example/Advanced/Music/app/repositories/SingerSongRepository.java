package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.Singer;
import example.Advanced.Music.app.entities.SingerSong;
import example.Advanced.Music.app.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SingerSongRepository extends JpaRepository<SingerSong, Long> {

    int countBySinger(Singer singer);
    List<SingerSong> findBySinger(Singer singer);

}
