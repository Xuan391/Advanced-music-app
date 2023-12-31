package example.Advanced.Music.app.services;

import example.Advanced.Music.app.entities.Role;
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
    public Optional<Role> findByRoleName(RoleEnum roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }


}
