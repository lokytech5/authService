package com.lokytech.authservice.client;

import com.lokytech.authservice.dto.UsersDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="user-service", url="localhost:8000")
public interface UsersClient {
    @GetMapping("/users/by-username/{username}")
    UsersDTO getUserByName(@PathVariable("username") String username);

}
