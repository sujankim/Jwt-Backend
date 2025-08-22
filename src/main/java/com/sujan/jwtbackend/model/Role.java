package com.sujan.jwtbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Role {

    @Id
    private String roleName;
    private String roleDescription;
}
