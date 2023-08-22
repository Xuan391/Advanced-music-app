package example.Advanced.Music.app.services;

import example.Advanced.Music.app.entities.Roles;
import example.Advanced.Music.app.enums.RoleEnum;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Roles> findByRoleName(RoleEnum roleName);
    List<Roles> findAll();

}
