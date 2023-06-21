package com.SDS.staffmanagement.entities;


import javax.persistence.*;

@Entity
public class HrEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int Id;

    private String name;

    private String role;

    private String email;

    private String password;

}
