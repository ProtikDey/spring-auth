package ksl.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ksl.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Slf4j
public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {


    private final JwtTokenService jwtTokenService;

    protected StatelessLoginFilter(String url, AuthenticationManager authenticationManager,
                                   JwtTokenService jwtTokenService) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authenticationManager);
        this.jwtTokenService = jwtTokenService;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException,
            IOException, ServletException {
        log.info("Login Request Received");

        ObjectMapper objectMapper = new ObjectMapper();
        UserPrincipal userPrincipal = null;

        try {
            userPrincipal = objectMapper.readValue(request.getInputStream(), UserPrincipal.class);
        } catch (Exception e) {
            log.error("Login request parsing error: {}", e);
            throw new InternalAuthenticationServiceException("Login request parsing error: {}", e);
        }

        validateLoginRequest(userPrincipal);

        String userNamePassword = userPrincipal.getUserName() + Constants.TOKEN_DELIMETER + userPrincipal.getPassword();
        String rawPassword = userPrincipal.getPassword();

        final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(userNamePassword,
                                                                                                    rawPassword);
        return getAuthenticationManager().authenticate(loginToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) {
        log.info("successfulAuthentication started");
        User principal = (User) authentication.getPrincipal();
        UserPrincipal user = UserPrincipal.builder()
                .userName(principal.getUsername())
                .password(principal.getPassword())
                .build();
        final UserAuthentication userAuthentication = new UserAuthentication(user);

        jwtTokenService.handleResponseHeader(request, response, user);

        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        response.setStatus(200);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException exception) {
        log.info("unsuccessfulAuthentication");
        response.setStatus(400);
    }


    private void validateLoginRequest(UserPrincipal user) {
        if (user.getUserName().isEmpty() || user.getUserName() == null ||
                user.getPassword().isEmpty() || user.getPassword() == null) {
            throw new InternalAuthenticationServiceException("Missing username or password");
        }
    }

}
