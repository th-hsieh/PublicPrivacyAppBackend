package org.Stasy.PublicPrivacyAppBackendHeroku.entity.model;


public class ResetPasswordResponse {
    private String email;
    private String newPassword;

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

