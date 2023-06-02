package com.SDS.staffmanagement.entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String dob;

//    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private ConfirmationTokenEntity confirmationTokenEntity;

    @NotBlank(message = "Name field is required !!")
    private String name;

    @NotBlank(message = "Father Name field is required !!")
    private String fatherName;
    @NotBlank(message = "Mother Name field is required !!")
    private String motherName;

    private String email;

    private String bloodGrp;

    private String gender;

    private String photo;

    private String role;

    private String identityProof;

    private String permanentAddress;

    private String presentAddress;

    private String mobileNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LeaveHistory> leave;

//    @Column(columnDefinition="String default Null")
    private String skillExperience;

//    @Column(columnDefinition="String default Null")
    private String dateOfJoining;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Project> projects;





}