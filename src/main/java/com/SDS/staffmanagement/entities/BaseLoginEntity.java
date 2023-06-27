package com.SDS.staffmanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BaseLoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int baseLoginId;
    private String name;
    private String email;
    private String password;
    private String role;
    private boolean approvedStatus = false;
}
