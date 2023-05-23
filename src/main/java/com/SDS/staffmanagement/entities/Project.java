package com.SDS.staffmanagement.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int projectId;

    private  String name;

    private String projectDescription;

    @ManyToOne
    @JoinColumn(name = "project")
    @JsonBackReference
    private User user;

    public Project() {
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + projectId +
                ", projects='" + name + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project(int id, String name, String projectDescription) {
        this.projectId = id;
        this.name = name;
        this.projectDescription = projectDescription;
    }

    public int getId() {
        return projectId;
    }

    public void setId(int id) {
        this.projectId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
}
