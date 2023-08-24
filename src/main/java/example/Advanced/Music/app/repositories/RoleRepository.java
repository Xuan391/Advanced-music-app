package example.Advanced.Music.app.repositories;

import example.Advanced.Music.app.entities.Role;
import example.Advanced.Music.app.enums.RoleEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByRoleName(RoleEnum name);
}
