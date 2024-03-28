package org.otus.dzmitry.kapachou.highload.model.authentication;

import lombok.Data;
import lombok.Getter;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.payload.AuthenticationTokens;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AuthenticatedPersonDetails implements UserDetails {
    @Getter
    private Person person;
    private Collection<? extends GrantedAuthority> authorities;
    private AuthenticationTokens authenticationTokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void wrapAuthorityRoles(Set<Role> personRoles) {
        authorities = personRoles.stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public List<String> getRoleList() {
        return person.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
