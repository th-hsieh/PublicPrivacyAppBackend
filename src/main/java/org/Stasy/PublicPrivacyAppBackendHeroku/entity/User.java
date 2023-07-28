package org.Stasy.PublicPrivacyAppBackendHeroku.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {


    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    @Column(name="username",nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String email;

    @Column(nullable = false)
    private boolean isEnabled;

    public User(Long userID, String username, String password, String email ) {
        this.userID=userID;
        this.username = username;
        this.password = password;
        this.email=email;
        this.isEnabled=isEnabled;
    }

    public User() {
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}