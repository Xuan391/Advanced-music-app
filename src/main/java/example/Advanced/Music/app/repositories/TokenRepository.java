package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByValue(String value);

    @Modifying
    @Query("delete from Token where userId = :userId")
    int deleteByUserId(@Param("userId") long userId);
}
