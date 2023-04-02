package ksl.api;


import ksl.entity.RoleEntity;
import ksl.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all/{id}")
    private Optional<RoleEntity> getAll(@PathVariable("id") Long id) {
        Optional<RoleEntity> roleEntities = roleRepository.findById(id);
        log.info(roleEntities.toString());
        return roleEntities;
    }
}
