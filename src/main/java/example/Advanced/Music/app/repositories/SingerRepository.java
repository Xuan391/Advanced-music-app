package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.Singer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface SingerRepository extends JpaRepository<Singer, Long>, JpaSpecificationExecutor<Singer> {
    Singer findByName(String name);

}
