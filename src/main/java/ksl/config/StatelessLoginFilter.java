package ksl.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.security.auth.login.LoginException;
import java.io.IOException;

@Slf4j
public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {


    protected StatelessLoginFilter(String url, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(url));

        setAuthenticationManager(authenticationManager);

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

        final UsernamePasswordAuthenticationToken loginToken =  new UsernamePasswordAuthenticationToken(userNamePassword,
                                                                            rawPassword);
        return getAuthenticationManager().authenticate(loginToken);
    }


    private void validateLoginRequest(UserPrincipal user) {
        if(user.getUserName().isEmpty() || user.getUserName() == null ||
                    user.getPassword().isEmpty() || user.getPassword() == null ){
            throw new InternalAuthenticationServiceException("Missing username or password");
        }
    }

}
