package com.staffmanagement.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String email;

    private String gender;

    private String mobileNumber;

    private String skills;

    private String password;

    private String role;

    private boolean isApproved = false;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Project> projects;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "base_login_id")
    private BaseLoginEntity baseLoginEntity;
}
