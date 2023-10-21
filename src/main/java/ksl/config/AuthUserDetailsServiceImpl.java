package ksl.config;

import ksl.entity.UserEntity;
import ksl.repository.UserRepository;
import ksl.util.Constants;
import ksl.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class AuthUserDetailsServiceImpl implements AuthUserDetailsService {

    private final UserRepository userRepository;
    public AuthUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public final UserDetails loadUserByUsername(String params) {
        log.info("Inside loadUserByUsername method with params: {}", params);

        String[] paramsArray = params.split(Constants.TOKEN_DELIMETER);

        String userName = paramsArray[0];
        String rawPassword = paramsArray[1];

        User.UserBuilder userBuilder = null;
        UserEntity userEntity = userRepository.findByUserName(userName);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found for user name: " + userName);
        }

        Boolean isPasswordMatched = false;
        if (userEntity.getPassword() != null) {
            isPasswordMatched = StringUtil.matchPassword(rawPassword, userEntity.getPassword());
        }

        if (!isPasswordMatched) {
            throw new InternalAuthenticationServiceException("Password mismatch. Login failed");
        }

        userBuilder = User.withUsername(userEntity.getUserName());
        UserDetails userDetails = userBuilder
                .password(userEntity.getPassword())
                .authorities(new ArrayList<GrantedAuthority>())
                .build();
        log.info("Generated User Details: {}", userDetails);
        return userDetails;
    }
}
