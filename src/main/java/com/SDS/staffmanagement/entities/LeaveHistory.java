package com.SDS.staffmanagement.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LeaveHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String fromDate;

    private String leaveType;

    private String toDate;

    private String description;

    private Boolean isApproved;

    private String approvedBy;

    private long currentMonthLeaves;

    private int totalLeaves;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}
