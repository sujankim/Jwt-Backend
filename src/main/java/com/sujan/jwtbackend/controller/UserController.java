package com.sujan.jwtbackend.controller;

import com.sujan.jwtbackend.model.User;
import com.sujan.jwtbackend.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/users"})
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostConstruct
    public void initRoleandUsers(){
        userService.initRolesAndUser();
    }

    @PostMapping
    public User registerNewUser(@RequestBody User user) {
       return userService.registerNewUser(user);
    }

    @GetMapping({"/forAdmin"})
    @PreAuthorize("hasRole('ADMIN')")
    public String forAdmin(){
        return "This URL is only accessible to admin";
    }

    @GetMapping({"/forUser"})
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String forUser(){
        return "This URL is only accessible to user";
    }
}
