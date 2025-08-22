package com.sujan.jwtbackend.service;

import com.sujan.jwtbackend.model.Role;
import com.sujan.jwtbackend.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role createNewRole(Role role) {
        return roleRepository.save(role);
    }
}
