package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long>, JpaSpecificationExecutor<Playlist> {
    List<Playlist> findByIdIn(List<Long> ids);
}
