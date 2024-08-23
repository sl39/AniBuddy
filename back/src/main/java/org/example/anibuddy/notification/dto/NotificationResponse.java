package org.example.anibuddy.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class NotificationResponse {

    private String notification_type;
    private String notified_at;
    private String title;
    private String content;
    private boolean isRead;
}
