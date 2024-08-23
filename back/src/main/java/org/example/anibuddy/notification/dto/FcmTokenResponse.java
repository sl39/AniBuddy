package org.example.anibuddy.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class FcmTokenResponse {

    private int clientId;
    private String clientRole;
    private String fcmToken;
}
