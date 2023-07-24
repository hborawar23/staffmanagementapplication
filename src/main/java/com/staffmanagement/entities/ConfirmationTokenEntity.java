package com.staffmanagement.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="confirmationToken")
public class ConfirmationTokenEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long tokenId;

        @Column(name = "confirmation_token")
        private String confirmationToken;

        @Temporal(TemporalType.TIMESTAMP)
        private Date createdDate;

        @OneToOne
        @JoinColumn(name = "user_confirmation_key_id")
        @JsonBackReference
        private User user;

}
