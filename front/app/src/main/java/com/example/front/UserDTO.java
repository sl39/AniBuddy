package com.example.front;

public class UserDTO {
    private String nickname, email, imageUrl;

    public UserDTO() {
    }

    public UserDTO(String nickname, String email, String imageUrl) {

        this.nickname = nickname;
        this.email = email;
        this.imageUrl = imageUrl;
    }
    public String getNickname() { return nickname; }

    public void setNickname(String nickname) { this.nickname = nickname; }


    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}