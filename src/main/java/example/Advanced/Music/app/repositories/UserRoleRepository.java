package example.Advanced.Music.app.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//@Repository
//@Transactional
//public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
//    @Modifying
//    @Query("delete from UserRole where userId = :userId")
//    int deleteByUserId(@Param("userId") long userId);
//}
