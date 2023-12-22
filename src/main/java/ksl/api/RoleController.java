package ksl.api;


import ksl.entity.RoleEntity;
import ksl.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleRepository roleRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    private Optional<RoleEntity> getById(@PathVariable("id") Long id) {
        Optional<RoleEntity> roleEntities = roleRepository.findById(id);
        log.info(roleEntities.toString());
        return roleEntities;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all")
    private List<RoleEntity> getAllRole() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        log.info(roleEntities.toString());
        return roleEntities;
    }
}
