package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.Song;
import example.Advanced.Music.app.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface SongRepository extends JpaRepository<Song, Long>, JpaSpecificationExecutor<Song> {
    Song findByName(String nameSong);
    List<Song> findByIdIn(List<Long> ids);
    @Query(value = "SELECT s FROM Song s WHERE s.name LIKE %:searchText%")
    List<Song> searchSongsByName(@Param("searchText") String searchText);
}
