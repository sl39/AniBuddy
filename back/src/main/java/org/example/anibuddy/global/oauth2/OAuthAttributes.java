package org.example.anibuddy.global.oauth2;

import lombok.Builder;
import lombok.Getter;
import org.example.anibuddy.global.oauth2.userinfo.KakoOAuth2UserInfo;
import org.example.anibuddy.global.oauth2.userinfo.OAuth2UserInfo;
import org.example.anibuddy.user.Role;
import org.example.anibuddy.user.SocialType;
import org.example.anibuddy.user.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {
    private static final Logger log = LoggerFactory.getLogger(OAuthAttributes.class);
    private String nameAttributeKey;
    private OAuth2UserInfo oAuth2UserInfo;

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuthAttributes of(SocialType  socialType, String  userNameAttributeName, Map<String, Object> attributes) {
        return ofKakao(userNameAttributeName,attributes);
    }

    private static OAuthAttributes ofKakao(final String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakoOAuth2UserInfo(attributes))
                .build();
    }

    public UserEntity toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo,String password) {
        return UserEntity.builder()
                .userName(oAuth2UserInfo.getNickname())
                .userPhone("")
                .socialType(socialType)
                .nickname(oAuth2UserInfo.getNickname())
                .imageUrl(oAuth2UserInfo.getImageUrl())
                .socialId(oAuth2UserInfo.getId())
                .email(UUID.randomUUID()+"@socialUser.com")
                .password(password)
                .role(Role.GUEST)
                .build();
    }

}
