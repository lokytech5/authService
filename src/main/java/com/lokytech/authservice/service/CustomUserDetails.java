package com.lokytech.authservice.service;

import com.lokytech.authservice.dto.RoleDTO;
import com.lokytech.authservice.dto.UsersDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private UsersDTO usersDTO;
    private Set<RoleDTO> roles;

    public CustomUserDetails() {
    }

    public CustomUserDetails(UsersDTO usersDTO) {
        this.usersDTO = usersDTO;
        this.roles = usersDTO.getRoles()
                .stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return usersDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return usersDTO.getUsername();
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
