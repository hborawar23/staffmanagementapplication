package com.SDS.staffmanagement.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int projectId;

    @NotEmpty
    private  String projectName;

    private String projectDescription;

    private int memberCount;

    private String managerName;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonBackReference
    private Manager manager;



    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<User> users;


}
