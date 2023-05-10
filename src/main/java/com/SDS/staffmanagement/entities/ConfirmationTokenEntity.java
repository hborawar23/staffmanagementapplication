package com.SDS.staffmanagement.entities;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="confirmationToken")
public class ConfirmationTokenEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "token_id")
        private long tokenid;

        @Column(name = "confirmation_token")
        private String confirmationToken;

        @Temporal(TemporalType.TIMESTAMP)
        private Date createdDate;

        @OneToOne
        @JoinColumn()
        private User user;
        public ConfirmationTokenEntity(User user) {
                this.user = user;
                createdDate = new Date();
                confirmationToken = UUID.randomUUID().toString();
        }

        public long getTokenid() {
                return tokenid;
        }

        public void setTokenid(long tokenid) {
                this.tokenid = tokenid;
        }

        public String getConfirmationToken() {
                return confirmationToken;
        }

        public void setConfirmationToken(String confirmationToken) {
                this.confirmationToken = confirmationToken;
        }

        public Date getCreatedDate() {
                return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
                this.createdDate = createdDate;
        }

        public User getUser() {
                return user;
        }

        public void setUser(User user) {
                this.user = user;
        }

        public ConfirmationTokenEntity(long tokenid, String confirmationToken, Date createdDate, User user) {
                this.tokenid = tokenid;
                this.confirmationToken = confirmationToken;
                this.createdDate = createdDate;
                this.user = user;
        }

        public ConfirmationTokenEntity() {
        }
}
