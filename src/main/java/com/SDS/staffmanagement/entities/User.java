package com.SDS.staffmanagement.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String dob;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    private String password;

    private String confirmationTokenEntity;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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

    @Column(columnDefinition="String default Null")
    private String skillExperience;

    @Column(columnDefinition="String default Null")
    private String dateOfJoining;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Project> projects;

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public String getSkillExperience() {
        return skillExperience;
    }

    public void setSkillExperience(String skillExperience) {
        this.skillExperience = skillExperience;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", dob='" + dob + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", enabled=" + enabled +
                ", password='" + password + '\'' +
                ", confirmationTokenEntity='" + confirmationTokenEntity + '\'' +
                ", name='" + name + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", email='" + email + '\'' +
                ", bloodGrp='" + bloodGrp + '\'' +
                ", gender='" + gender + '\'' +
                ", photo='" + photo + '\'' +
                ", role='" + role + '\'' +
                ", identityProof='" + identityProof + '\'' +
                ", permanentAddress='" + permanentAddress + '\'' +
                ", presentAddress='" + presentAddress + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", skillExperience='" + skillExperience + '\'' +
                ", dateOfJoining='" + dateOfJoining + '\'' +
                ", projects=" + projects +
                '}';
    }

    public User(int id, String dob, String verificationCode, boolean enabled, String password, String confirmationTokenEntity, String name, String fatherName, String motherName, String email, String bloodGrp, String gender, String photo, String role, String identityProof, String permanentAddress, String presentAddress, String mobileNumber, String skillExperience, String dateOfJoining, List<Project> projects) {
        this.id = id;
        this.dob = dob;
        this.verificationCode = verificationCode;
        this.enabled = enabled;
        this.password = password;
        this.confirmationTokenEntity = confirmationTokenEntity;
        this.name = name;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.email = email;
        this.bloodGrp = bloodGrp;
        this.gender = gender;
        this.photo = photo;
        this.role = role;
        this.identityProof = identityProof;
        this.permanentAddress = permanentAddress;
        this.presentAddress = presentAddress;
        this.mobileNumber = mobileNumber;
        this.skillExperience = skillExperience;
        this.dateOfJoining = dateOfJoining;
        this.projects = projects;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIdentityProof() {
        return identityProof;
    }

    public void setIdentityProof(String identityProof) {
        this.identityProof = identityProof;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmationTokenEntity() {
        return confirmationTokenEntity;
    }

    public void setConfirmationTokenEntity(String confirmationTokenEntity) {
        this.confirmationTokenEntity = confirmationTokenEntity;
    }
}