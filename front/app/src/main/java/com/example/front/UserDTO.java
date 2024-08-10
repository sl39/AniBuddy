package com.example.front;

public class UserDTO {
    private String userName, email, userAddress;

    public UserDTO() {
    }

    public UserDTO(String userName, String email, String userAddress) {
        this.userName = userName;
        this.email = email;
        this.userAddress = userAddress;
    }
    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getUserAddress() { return userAddress; }

    public void setUserAddress(String userAddress) { this.userAddress = userAddress; }

}