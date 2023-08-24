package example.Advanced.Music.app.services;

import example.Advanced.Music.app.entities.Role;
import example.Advanced.Music.app.enums.RoleEnum;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByRoleName(RoleEnum roleName);
    List<Role> findAll();

}
