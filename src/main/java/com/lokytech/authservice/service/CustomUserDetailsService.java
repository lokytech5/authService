package com.lokytech.authservice.service;

import com.lokytech.authservice.dto.UsersDTO;
import com.lokytech.authservice.validation.UserExistenceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserExistenceValidator userExistenceValidator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersDTO userDto = userExistenceValidator.validateUserExists(username);
        return new CustomUserDetails(userDto);
    }
}
