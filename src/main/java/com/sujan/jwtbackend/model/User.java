package com.sujan.jwtbackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class User {

    @Id
    private String userName;
    private String userFirstName;
    private String userLastName;
    private String userPassword;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "USER_ROLE",
    joinColumns ={
            @JoinColumn(name = "USER_ID")
    },
            inverseJoinColumns ={
            @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> role;
}
