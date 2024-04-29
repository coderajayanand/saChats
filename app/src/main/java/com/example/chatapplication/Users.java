package com.example.chatapplication;

public class Users {
    String userId, userName, email, password, profilePic, lastMessage, status;
    Boolean hug;

    public Users(){}

    public Users(String userId, String userName, String email, String password, String profilePic, String status) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profilePic = profilePic;
        this.status = status;
        hug = false;
    }

    public Boolean getHug() {
        return hug;
    }

    public void setHug(Boolean hug) {
        this.hug = hug;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}