package com.zorvyn.financedashboard.security;

import com.zorvyn.financedashboard.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        user.getRole().getAuthorities().forEach( auth ->
                authorities.add(
                        new SimpleGrantedAuthority(auth.getName())
                )
        );
        return authorities;
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public String getUsername(){
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return !user.isLocked();
    }

    @Override
    public boolean isEnabled(){
        return user.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    public User getUser(){
        return user;
    }
}
