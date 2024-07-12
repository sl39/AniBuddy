package org.example.anibuddy.global.oauth2.userinfo;

import java.util.Map;

public class KakoOAuth2UserInfo extends OAuth2UserInfo{

    public KakoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId(){
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getNickname() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("Kakao_account");
        if (account == null) {
            return null;
        }

        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (profile == null) {
            return null;
        }

        return (String) profile.get("nuickname");
    }

    @Override
    public String getImageUrl(){
        Map<String, Object> account = (Map<String, Object>) attributes.get("Kakao_account");

        if (account == null) {
            return null;
        }
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (profile == null) {
            return null;
        }
        return (String) profile.get("thumbnail_image_url");
    }
}
