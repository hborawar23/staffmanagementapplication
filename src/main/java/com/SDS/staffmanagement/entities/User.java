package com.SDS.staffmanagement.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
    @Size(min = 3, max = 25, message = "name should between 3 to 20 characters !!")
    private String name;

    @Column(unique = true)
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "invalid email!!")
    private String email;

    private String gender;

    private String role;

    private String identityProof;

    private boolean isApproved;

    @NotBlank
    private String permanentAddress;

    @NotBlank
    private String presentAddress;

    @NotNull
    @Size(min = 10,max = 10)
    @Pattern(regexp = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$", message = "invalid contact")
    private String mobileNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LeaveHistory> leave;

//    @Column(columnDefinition="String default Null")
//    private String skillExperience;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<Project> projects;

    @ManyToOne
    @JoinTable(name = "project_id")
    @JsonBackReference
    private Project project;

}