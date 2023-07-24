package com.staffmanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BaseLoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int baseLoginId;

    private String role;

    private boolean approvedStatus = false;

//    private int userId;
//    private String name;
//    private String email;
//    private String password;

//    @OneToOne(mappedBy = "manager")
//    private  Manager manager;

    @OneToOne(mappedBy = "baseLoginEntity")
    private User user;

    @OneToOne(mappedBy = "baseLoginEntity")
    private Manager manager;
}
