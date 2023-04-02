package ksl.service;

import ksl.entity.RoleEntity;
import ksl.entity.UserEntity;
import ksl.model.request.UserRegistrationRequest;
import ksl.model.response.UserRegistrationResponse;
import ksl.repository.RoleRepository;
import ksl.repository.UserRepository;
import ksl.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        if (!userRepository.findAllByuserName(request.getUserName()).isEmpty()) {
            log.info("User reg failed as duplicate username detected : {}", request.getUserName());
            return UserRegistrationResponse.builder()
                    .status("Failed")
                    .message("User name already exists")
                    .build();
        }

        if (!userRepository.findAllByemail(request.getEmail()).isEmpty()) {
            log.info("User reg failed as duplicate email detected : {}", request.getEmail());
            return UserRegistrationResponse.builder()
                    .status("Failed")
                    .message("Email already exists")
                    .build();
        }

        UserEntity userEntity = UserEntity.builder()
                .name(request.getName())
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(StringUtil.encodePassword(request.getPassword()))
                .build();

        if (request.getRoleId() != null) {
            RoleEntity role = roleRepository.getReferenceById(request.getRoleId());
            userEntity.setRoleEntity(role);
        }

        UserEntity createdUser = userRepository.save(userEntity);

        log.info("User created successfully with id {}", createdUser.getId());

        return UserRegistrationResponse.builder()
                .status("Success")
                .message("User registration successful")
                .id(createdUser.getId())
                .build();
    }

}


