package com.example.front;

public class UserDTO {
    private String userName, email, imageUrl;

    public UserDTO() {
    }

    public UserDTO(String nickname, String email, String imageUrl) {
        this.userName = nickname;
        this.email = email;
    }
    public String getNickname() { return userName; }

    public void setNickname(String nickname) { this.userName = nickname; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}