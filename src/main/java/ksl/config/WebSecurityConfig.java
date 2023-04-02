package ksl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final AuthUserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;

    public WebSecurityConfig(AuthUserDetailsService userDetailsService, JwtTokenService jwtTokenService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeHttpRequests()
                .requestMatchers("./").permitAll()
                .and()
                .addFilterBefore(new StatelessLoginFilter("/api/login", authManager(), jwtTokenService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public AuthenticationManager authManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider::authenticate;
    }


}
