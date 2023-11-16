package com.lokytech.authservice.validation;

import com.lokytech.authservice.client.UsersClient;
import com.lokytech.authservice.dto.UsersDTO;
import com.lokytech.authservice.exception.ExternalServiceException;
import com.lokytech.authservice.exception.UserNotFoundException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserExistenceValidator {
    private static final Logger logger = LoggerFactory.getLogger(UserExistenceValidator.class);

    @Autowired
    UsersClient usersClient;

    public UsersDTO validateUserExists(String username){
        UsersDTO user = null;
        try {
            user = usersClient.getUserByName(username);
            if (user == null) {
                throw new UserNotFoundException("User with name " + username + " not found in user-service.");
            }
            return user;
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new UserNotFoundException("User with name " + username + " not found in user-service.");
            } else {
                throw new ExternalServiceException("Error occurred when calling user-service: " + e.getMessage(), e);
            }
        }
    }
}
