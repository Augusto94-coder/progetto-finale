package org.lessons.java.progetto_finale.security;


import org.lessons.java.progetto_finale.model.Role;
import org.lessons.java.progetto_finale.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DatabaseUserDetails implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authoritySet;

    public DatabaseUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authoritySet = new HashSet<GrantedAuthority>();
        for (Role userRole : user.getRoles()) {
            authoritySet.add(new SimpleGrantedAuthority(userRole.getName()));
        }
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authoritySet;
    }
  
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public Set<GrantedAuthority> getAuthoritySet() {
        return authoritySet;
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
}
