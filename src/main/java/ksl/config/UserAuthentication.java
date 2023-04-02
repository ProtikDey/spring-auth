package ksl.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthentication implements Authentication {

    private final UserPrincipal userPrincipal;
    private boolean authenticated = true;

    public UserAuthentication(UserPrincipal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return userPrincipal.getPassword();
    }

    @Override
    public Object getDetails() {
        return userPrincipal;
    }

    @Override
    public Object getPrincipal() {
        return userPrincipal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return userPrincipal.getUserName();
    }
}
