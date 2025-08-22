package com.sujan.jwtbackend.service;

import com.sujan.jwtbackend.model.Role;
import com.sujan.jwtbackend.model.User;
import com.sujan.jwtbackend.repository.RoleRepository;
import com.sujan.jwtbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public User registerNewUser(@RequestBody User user) {

        Role role = roleRepository.findById("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRole(roles);

        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        return userRepository.save(user);
    }

    public void initRolesAndUser(){

        // Create ADMIN role if not exists
        Role adminRole = roleRepository.findById("ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setRoleName("ADMIN");
            role.setRoleDescription("Admin role with all privileges");
            return roleRepository.save(role);
        });

        // Create USER role if not exists
        Role userRole = roleRepository.findById("USER").orElseGet(() -> {
            Role role = new Role();
            role.setRoleName("USER");
            role.setRoleDescription("Default role for newly created users");
            return roleRepository.save(role);
        });

        if (!userRepository.existsById("admin123")) {
            User adminUser = new User();
            adminUser.setUserFirstName("admin");
            adminUser.setUserLastName("admin");
            adminUser.setUserName("admin123");
            adminUser.setUserPassword(getEncodedPassword("admin@pass"));
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminUser.setRole(adminRoles);
            userRepository.save(adminUser);
        }

        /*User user = new User();
        user.setUserFirstName("Sujan");
        user.setUserLastName("Lamichhane");
        user.setUserName("sujan123");
        user.setUserPassword(getEncodedPassword("sujan@pass"));
        Set<Role> UserRoles = new HashSet<>();
        UserRoles.add(userRole);
        user.setRole(UserRoles);
        userRepository.save(user);*/
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
