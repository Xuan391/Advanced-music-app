package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.Singer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface SingerRepository extends JpaRepository<Singer, Long>, JpaSpecificationExecutor<Singer> {
    @Override
    Optional<Singer> findById(Long aLong);

    Optional<Singer> findByName(String name);

    Boolean existsByName (String name);
    List<Singer> findByIdIn (List<Long> ids);
    @Query(value = "SELECT si FROM  Singer si WHERE si.name LIKE %:searchText%")
    List<Singer> searchSingerByByName(@Param("searchText") String searchText);

}
