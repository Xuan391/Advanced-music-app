package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.ListenedHistory;
import example.Advanced.Music.app.entities.Singer;
import example.Advanced.Music.app.entities.Song;
import example.Advanced.Music.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListenedHistoryRepository extends JpaRepository<ListenedHistory, Long> {
    List<Song> findByUserOrderByCreatedDateDesc(User user);
}
