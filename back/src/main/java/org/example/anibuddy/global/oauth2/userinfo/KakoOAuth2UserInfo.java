package org.example.anibuddy.global.oauth2.userinfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KakoOAuth2UserInfo extends OAuth2UserInfo{

    private static final Logger log = LoggerFactory.getLogger(KakoOAuth2UserInfo.class);

    public KakoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId(){
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getNickname() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        
        if (account == null) {
            

            return null;
        }

        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (profile == null) {
            return null;
        }

        return (String) profile.get("nickname");
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
