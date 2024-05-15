package com.sdi.hostedin.data.model;

public class NewPasswordRecovery {
    private String newPassword;
    private String email;

    public NewPasswordRecovery(String newPassword, String email) {
        this.newPassword = newPassword;
        this.email = email;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
