package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.SearchHistory;
import example.Advanced.Music.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findTop10ByUserOrderByCreatedDateDesc(User user);
}
