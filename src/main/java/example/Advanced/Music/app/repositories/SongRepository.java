package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.Song;
import example.Advanced.Music.app.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface SongRepository extends JpaRepository<Song, Long>, JpaSpecificationExecutor<Song> {
    Song findByName(String nameSong);
    List<Song> findByIdIn(List<Long> ids);
}
