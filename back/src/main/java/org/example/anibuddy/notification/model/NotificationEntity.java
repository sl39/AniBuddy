package org.example.anibuddy.notification.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.user.UserEntity;

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

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private OwnerEntity toOwner;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity toUser;

    @Column(nullable = false)
    private String toRole;

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
            String content,
            String toRole,
            UserEntity toUser,
            OwnerEntity toOwner
    ){
        this.notification_type = type;
        this.title = title;
        this.content = content;
        this.toRole = toRole;
        this.toUser = toUser;
        this.toOwner = toOwner;
    }
}
