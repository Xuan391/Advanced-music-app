package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.UserDownloadSong;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface UserDownloadSongRepository extends JpaRepository<UserDownloadSong, Long>, JpaSpecificationExecutor<UserDownloadSong> {
    List<UserDownloadSong> findByUserId(long UserId);
}
