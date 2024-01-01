package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    List<User> findByIdIn(List<Long> ids);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndEmail(String username, String email);

    Boolean existsByUsername(String username);

    @Modifying
    int deleteByIdIn(List<String> ids);

    @Modifying
    @Query("update User u set u.failLoginCount = 0 where u.username = :username")
    void loginSuccess(@Param("username") String username);

    @Modifying
    @Query("update User u set u.isLock = true where u.username = :username")
    void lockUser(@Param("username") String username);

    @Modifying
    @Query("update User u set u.isLock = false where u.username = :username ")
    void unLockUser(@Param("username") String username);

    @Modifying
    @Query("update User u set u.failLoginCount = u.failLoginCount + 1 where u.username = :username")
    void failLogin(@Param("username") String username);

    @Modifying
    @Query("update User u set u.password = :pwd where u.id = :id")
    int updatePassword(@Param("pwd") String pwd, @Param("id") long id);

    @Query(value = "SELECT u FROM  User u WHERE u.username LIKE concat('%', ?1, '%') ")
    List<User> searchUsersByName(@Param("searchText") String searchText);
}
