package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.OTP;
import example.Advanced.Music.app.enums.OTPType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Transactional
public interface OTPRepository extends JpaRepository<OTP, String> {
    List<OTP> findByUserId(long userId);

    List<OTP> findByUserIdAndType(Long userId, OTPType type);

    @Modifying
    @Query("delete from OTP where userId = :userId")
    int deleteByUserId(@Param("userId") long userId);
}
