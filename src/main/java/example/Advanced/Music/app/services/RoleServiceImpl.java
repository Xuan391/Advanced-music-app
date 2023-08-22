package example.Advanced.Music.app.services;

import example.Advanced.Music.app.entities.Roles;
import example.Advanced.Music.app.enums.RoleEnum;
import example.Advanced.Music.app.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Optional<Roles> findByRoleName(RoleEnum roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public List<Roles> findAll() {
        return roleRepository.findAll();
    }


}
