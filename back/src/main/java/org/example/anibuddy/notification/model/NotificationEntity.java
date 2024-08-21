package org.example.anibuddy.notification.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification_entity")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDateTime notified_at = LocalDateTime.now();

    @Column(nullable = false)
    private String notification_type; //현재는 RESERVATION 타입만 있음

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isRead = false;

    public NotificationEntity(
            String type,
            String title,
            String content
    ){
        this.notification_type = type;
        this.title = title;
        this.content = content;
    }

}
